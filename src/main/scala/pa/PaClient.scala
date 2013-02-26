package pa


import Parser._
import org.joda.time.{DateTime, DateMidnight}

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads6.pa-sport.com"

  def competitions: List[Season] =
    parseCompetitions(get("/api/football/competitions/competitions/%s" format apiKey))

  def matchEvents(id: String): Option[MatchEvents] = parseMatchEvents(
    get("/api/football/match/events/%s/%s".format(apiKey, id))
  )

  def matchStats(id: String): Seq[MatchStats] =
    parseMatchStats(get("/api/football/match/stats/%s/%s".format(apiKey, id)))

  def lineUp(id: String): LineUp =
    parseLineUp(get("/api/football/match/lineUps/%s/%s".format(apiKey, id)))

  def matchDay(date: DateMidnight): List[MatchDay] =
    parseMatchDay(
      get("/api/football/competitions/matchDay/%s/%s".format(apiKey, paDateString(date)))
    )
    
  def matchDay(competitionId: String, date: DateMidnight): List[MatchDay] =
    parseMatchDay(
      get("/api/football/competition/matchDay/%s/%s/%s".format(apiKey, competitionId, paDateString(date)))
    )

  def matchDay(id: String): Option[MatchDay] =
    parseMatchDay(
      get("/api/football/match/info/%s/%s".format(apiKey, id))
    ).headOption
    
  def results(start: DateMidnight): List[Result] = resultsAll(start, None)

  def results(start: DateMidnight, end: DateMidnight): List[Result] = resultsAll(start, Some(end))

  private def resultsAll(start: DateMidnight, end: Option[DateMidnight] = None): List[Result] = {
    val dateStr = paRangeString(start, end)
    parseResults(
      get("/api/football/competitions/results/%s/%s".format(apiKey, dateStr))
    )
  }

  def results(competitionId: String, start: DateMidnight): List[Result] = results(competitionId, start, None)

  def results(competitionId: String, start: DateMidnight, end: DateMidnight): List[Result] =
    results(competitionId, start, Some(end))

  private def results(competitionId: String, start: DateMidnight, end: Option[DateMidnight] = None): List[Result] ={
    val dateStr = paRangeString(start, end)
    parseResults(
      get("/api/football/competition/results/%s/%s/%s".format(apiKey, competitionId, dateStr))
    )
  }

  def leagueTable(competitionId: String, date: DateMidnight): List[LeagueTableEntry] =
    parseLeagueTable(
      get("/api/football/competition/leagueTable/%s/%s/%s".format(apiKey, competitionId, paDateString(date)))
    )

  def fixtures(): List[Fixture] = {
    parseFixtures(get("/api/football/competitions/fixtures/%s" format (apiKey)))
  }
  
  def fixtures(competitionId: String): List[Fixture] = {
    parseFixtures(get("/api/football/competition/fixtures/%s/%s" format (apiKey, competitionId)))
  }

  def liveMatches(competitionId: String): List[LiveMatch] = {
    parseLiveMatches(get("/api/football/competition/liveGames/%s/%s" format (apiKey, competitionId)))
  }

  def eaIndex(competitionId: String, startDate: DateMidnight, endDate: DateMidnight): List[EAIndexPlayer] = {
    parseEaIndex(get("/api/football/competition/eaindex/%s/%s/%s/%s".format(
      apiKey,
      competitionId,
      paDateString(startDate),
      paDateString(endDate))))
  }

  /** String format for dates in PA urls */
  protected def paDateString(date: DateMidnight): String = {
    date.toString("yyyyMMdd")
  }

  /** String format for date ranges in PA urls */
  protected def paRangeString(start: DateMidnight, end: Option[DateMidnight]): String = {
    paDateString(start) + (end map { "/" + paDateString(_)} getOrElse "")
  }

  protected def get(suffix: String): String = GET(base + suffix) match {
    case Response(200, body, _) =>  body
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }
}

class PaClientException(msg: String) extends RuntimeException(msg)
