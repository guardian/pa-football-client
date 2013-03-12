package pa

import concurrent.{Future, Promise}

trait Http {
  def GET(url: String): Future[Response]
}

case class Response(status: Int, body: String, statusLine: String)


