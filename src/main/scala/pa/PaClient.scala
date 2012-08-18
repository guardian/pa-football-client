package pa


import Parser._

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads2.pa-sport.com"

  def competitions: Seq[Season] = GET(base + "/api/football/competitions/%s/json".format(apiKey)) match {
    case Response(200, body, _) =>  parseCompetitions(body)
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }

}

case class Response(status: Int, body: String, statusLine: String)

trait Http {
  def GET(url: String): Response
}

class PaClientException(msg: String) extends RuntimeException(msg)
