package pa


import Parser._

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads2.pa-sport.com"

  def competitions: Seq[Season] = GET(base + "/api/football/competitions/%s/json".format(apiKey)) match {
    case Response(200, body, _) =>  parseCompetitions(body)
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }

  def matchEvents(id: String): Match = GET(base + "/api/football/match/events/%s/%s/json".format(apiKey, id)) match {
    case Response(200, body, _) =>  parseMatchEvents(body)
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }

  def matchStats(id: String): MatchStats = GET(base + "/api/football/match/stats/%s/%s/json".format(apiKey, id)) match {
    case Response(200, body, _) =>  parseMatchStats(body)
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }

}

class PaClientException(msg: String) extends RuntimeException(msg)
