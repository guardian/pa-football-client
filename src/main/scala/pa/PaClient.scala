package pa


import Parser._
import org.joda.time.DateMidnight

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads6.pa-sport.com"

  def competitions: List[Season] = parseCompetitions(get("/api/football/competitions/competitions/%s" format apiKey))

  def matchEvents(id: String): Option[MatchEvents] = parseMatchEvents(
    get("/api/football/match/events/%s/%s".format(apiKey, id))
  )

  def matchStats(id: String): Option[MatchStats] = parseMatchStats(get("/api/football/match/stats/%s/%s".format(apiKey, id)))

  def matchDay(date: DateMidnight): List[MatchDay] =
    parseMatchDay(
      get("/api/football/competitions/matchDay/%s/%s".format(apiKey, date.toString("yyyyMMdd")))
    )
    
  def matchDay(competitionId: String, date: DateMidnight): List[MatchDay] =
    parseMatchDay(
      get("/api/football/competition/matchDay/%s/%s/%s".format(apiKey, competitionId, date.toString("yyyyMMdd")))
    )

  def results(competitionId: String, start: DateMidnight): List[Result] = results(competitionId, start, None)

  def results(competitionId: String, start: DateMidnight, end: DateMidnight): List[Result] =
    results(competitionId, start, Some(end))

  private def results(competitionId: String, start: DateMidnight, end: Option[DateMidnight] = None): List[Result] ={
    val dateStr = start.toString("yyyyMMdd") + (end map { "/" + _.toString("yyyyMMdd")} getOrElse "")
    parseResults(
      get("/api/football/competition/results/%s/%s/%s".format(apiKey, competitionId, dateStr))
    )
  }

  def leagueTable(competitionId: String, date: DateMidnight): List[LeagueTableEntry] =
    parseLeagueTable(
      get("/api/football/competition/leagueTable/%s/%s/%s".format(apiKey, competitionId, date.toString("yyyyMMdd")))
    )

  def fixtures(competitionId: String): List[Fixture] = {
    parseFixtures(get("/api/football/competition/fixtures/%s/%s" format (apiKey, competitionId)))
  }

  protected def get(suffix: String): String = GET(base + suffix) match {
    case Response(200, body, _) =>  body
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }
}

class PaClientException(msg: String) extends RuntimeException(msg)
