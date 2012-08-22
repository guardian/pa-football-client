package pa


import Parser._
import org.joda.time.DateMidnight

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads2.pa-sport.com"

  def competitions: Seq[Season] = get("/api/football/competitions/%s/json".format(apiKey)){
    parseCompetitions
  }

  def matchEvents(id: String): MatchEvents = get("/api/football/match/events/%s/%s/json".format(apiKey, id)) {
    parseMatchEvents
  }

  def matchStats(id: String): MatchStats = get("/api/football/match/stats/%s/%s/json".format(apiKey, id)) {
    parseMatchStats
  }

  def matchDay(competitionId: String, date: DateMidnight): Seq[MatchDay] =
    get("/api/football/competition/matchDay/%s/%s/%s/json".format(apiKey, competitionId, date.toString("yyyyMMdd"))) {
      parseMatchDay
  }

  private def get[T](path: String)(parser: String => T) = GET(base + path) match {
    case Response(200, body, _) => parser(body)
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }
}

class PaClientException(msg: String) extends RuntimeException(msg)
