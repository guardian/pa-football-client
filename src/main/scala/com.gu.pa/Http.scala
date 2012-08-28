package com.gu.pa

import com.ning.http.client.{AsyncHttpClient, ProxyServer, AsyncHttpClientConfig}
import com.ning.http.client.providers.netty.{NettyAsyncHttpProvider, NettyConnectionsPool}
import dispatch.{url, FunctionHandler}


trait Http {
  def GET(url: String): Response
}

case class Response(status: Int, body: String, statusLine: String)

case class Proxy(host: String, port: Int)

trait DispatchHttp extends Http {

  lazy val maxConnections: Int = 10
  lazy val connectionTimeoutInMs: Int = 1000
  lazy val requestTimeoutInMs: Int = 2000
  lazy val proxy: Option[Proxy] = None
  lazy val compressionEnabled: Boolean = true

  lazy val config = {
    val c = new AsyncHttpClientConfig.Builder()
      .setAllowPoolingConnection(true)
      .setMaximumConnectionsPerHost(maxConnections)
      .setMaximumConnectionsTotal(maxConnections)
      .setConnectionTimeoutInMs(connectionTimeoutInMs)
      .setRequestTimeoutInMs(requestTimeoutInMs)
      .setCompressionEnabled(compressionEnabled)
      .setFollowRedirects(true)
    proxy.foreach(p => c.setProxyServer(new ProxyServer(p.host, p.port)))
    c.build
  }

  object Client extends dispatch.Http {
    override lazy val client = {
      val connectionPool = new NettyConnectionsPool(new NettyAsyncHttpProvider(config))
      new AsyncHttpClient(new AsyncHttpClientConfig.Builder(config).setConnectionsPool(connectionPool).build)
    }
  }

  def GET(urlString: String): Response = {

    val request = url(urlString).build

    Client(request, httpResponseHandler)()
  }

  def httpResponseHandler = new FunctionHandler(r =>
    Response(r.getStatusCode, r.getResponseBody("utf-8"), r.getStatusText)
  )

  def close() = Client.client.close()
}
