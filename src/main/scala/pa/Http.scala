package pa

import scala.concurrent.Future

trait Http {
  def GET(url: String): Future[Response]
}

case class Response(status: Int, body: String, statusLine: String)


