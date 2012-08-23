package pa


import Parser._
import org.joda.time.DateMidnight

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads2.pa-sport.com"

  def competitions: List[Season] = parseCompetitions(get("/api/football/competitions/%s/json" format apiKey))

  def matchEvents(id: String): MatchEvents = parseMatchEvents(get("/api/football/match/events/%s/%s/json".format(apiKey, id)))

  def matchStats(id: String): MatchStats = parseMatchStats(get("/api/football/match/stats/%s/%s/json".format(apiKey, id)))

  def matchDay(competitionId: String, date: DateMidnight): List[MatchDay] =
    parseMatchDay(get("/api/football/competition/matchDay/%s/%s/%s/json".format(apiKey, competitionId, date.toString("yyyyMMdd"))))


  def leagueTable(competitionId: String, date: DateMidnight): List[LeagueTableEntry] =
    parseLeagueTable(get("/api/football/competition/leagueTable/%s/%s/%s/json".format(apiKey, competitionId, date.toString("yyyyMMdd"))))

  protected def get(suffix: String): String = GET(base + suffix) match {
    case Response(200, body, _) =>  body
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }
}

class PaClientException(msg: String) extends RuntimeException(msg)
