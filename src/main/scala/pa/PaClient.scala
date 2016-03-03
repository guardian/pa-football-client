package pa


import Parser._
import org.joda.time.LocalDate
import concurrent.{ExecutionContext, Future, Promise}

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads6.pa-sport.com"

  def competitions(implicit context: ExecutionContext): Future[List[Season]] =
    get(s"/api/football/competitions/competitions/$apiKey").map(interceptErrors).map(parseCompetitions)

  def matchInfo(id: String)(implicit context: ExecutionContext): Future[MatchDay] =
    get(s"/api/football/match/info/$apiKey/$id").map(interceptErrors).map(parseMatchInfo)

  def matchEvents(id: String)(implicit context: ExecutionContext): Future[Option[MatchEvents]] =
    get(s"/api/football/match/events/$apiKey/$id").map(interceptErrors).map(parseMatchEvents)

  def matchStats(id: String)(implicit context: ExecutionContext): Future[Seq[MatchStats]] =
    get(s"/api/football/match/stats/$apiKey/$id").map(interceptErrors).map(parseMatchStats)

  def lineUp(id: String)(implicit context: ExecutionContext): Future[LineUp] = get(s"/api/football/match/lineUps/$apiKey/$id").map(interceptErrors).map(parseLineUp)

  def matchDay(date: LocalDate)(implicit context: ExecutionContext): Future[List[MatchDay]] =
    get(s"/api/football/competitions/matchDay/$apiKey/${date.toString("yyyyMMdd")}").map(interceptErrors).map(parseMatchDay)
  def matchDay(competitionId: String, date: LocalDate)(implicit context: ExecutionContext): Future[List[MatchDay]] =
    get(s"/api/football/competition/matchDay/$apiKey/$competitionId/${date.toString("yyyyMMdd")}").map(interceptErrors).map(parseMatchDay)

  def results(start: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] = resultsAll(start, None)
  def results(start: LocalDate, end: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] = resultsAll(start, Some(end))

  private def resultsAll(start: LocalDate, end: Option[LocalDate] = None)(implicit context: ExecutionContext): Future[List[Result]] = {
    val dateStr = start.toString("yyyyMMdd") + (end map { e => s"/${e.toString("yyyyMMdd")}"} getOrElse "")
    get(s"/api/football/competitions/results/$apiKey/$dateStr").map(interceptErrors).map(parseResults)
  }

  def results(competitionId: String, start: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] = results("competition", competitionId, start, None)
  def results(competitionId: String, start: LocalDate, end: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] =
    results("competition", competitionId, start, Some(end))

  def teamResults(teamId: String, start: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] =
    results("team", teamId, start, None)
  def teamResults(teamId: String, start: LocalDate, end: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] =
    results("team", teamId, start, Some(end))

  private def results(resultType: String, competitionId: String, start: LocalDate, end: Option[LocalDate] = None)(implicit context: ExecutionContext): Future[List[Result]] ={
    val dateStr = start.toString("yyyyMMdd") + (end map { e => s"/${e.toString("yyyyMMdd")}" } getOrElse "")
    get(s"/api/football/$resultType/results/$apiKey/$competitionId/$dateStr").map(interceptErrors).map(parseResults)
  }

  def leagueTable(competitionId: String, date: LocalDate)(implicit context: ExecutionContext): Future[List[LeagueTableEntry]] =
      get(s"/api/football/competition/leagueTable/$apiKey/$competitionId/${date.toString("yyyyMMdd")}").map(interceptErrors).map(parseLeagueTable)


  def fixtures(implicit context: ExecutionContext): Future[List[Fixture]] = get(s"/api/football/competitions/fixtures/$apiKey").map(interceptErrors).map(parseFixtures)
  def fixtures(competitionId: String)(implicit context: ExecutionContext): Future[List[Fixture]] =
    get(s"/api/football/competition/fixtures/$apiKey/$competitionId").map(interceptErrors).map(parseFixtures)

  def liveMatches(competitionId: String)(implicit context: ExecutionContext): Future[List[LiveMatch]] =
    get(s"/api/football/competition/liveGames/$apiKey/$competitionId").map(interceptErrors).map(parseLiveMatches)

  def teamHead2Head(team1Id: String, team2Id: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/team/headToHeads/$apiKey/$team1Id/$team2Id/$startDateStr/$endDateStr").map(interceptErrors).map(parseTeamHead2Head)
  }
  def teamHead2Head(team1Id: String, team2Id: String, startDate: LocalDate, endDate: LocalDate, competitionId: String)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/team/headToHeads/$apiKey/$team1Id/$team2Id/$startDateStr/$endDateStr/$competitionId").map(interceptErrors).map(parseTeamHead2Head)
  }

  def teamEvents(teamId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[List[TeamEventMatch]] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/team/events/$apiKey/$teamId/$startDateStr/$endDateStr").map(interceptErrors).map(parseTeamEventMatches)
  }

  def squad(teamId: String)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    get(s"/api/football/team/squad/$apiKey/$teamId").map(interceptErrors).map(parseSquad)
  }
  def squad(teamId: String, startDate: LocalDate)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    get(s"/api/football/team/squad/$apiKey/$teamId/$startDateStr").map(interceptErrors).map(parseSquad)
  }
  def squad(teamId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/team/squad/$apiKey/$teamId/$startDateStr/$endDateStr").map(interceptErrors).map(parseSquad)
  }

  def teams(competitionId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[List[Team]] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/competition/teams/$apiKey/$competitionId/$startDateStr/$endDateStr").map(interceptErrors).map(parseTeams)
  }

  def playerHead2Head(player1Id: String, player2Id: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/headToHeads/$apiKey/$player1Id/$player2Id/$startDateStr/$endDateStr").map(interceptErrors).map(parsePlayerHead2Head)
  }
  def playerHead2Head(player1Id: String, player2Id: String, startDate: LocalDate, endDate: LocalDate, competitionId: String)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/headToHeads/$apiKey/$player1Id/$player2Id/$startDateStr/$endDateStr/$competitionId").map(interceptErrors).map(parsePlayerHead2Head)
  }

  def appearances(playerId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[PlayerAppearances] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/appearances/$apiKey/$playerId/$startDateStr/$endDateStr").map(interceptErrors).map(parsePlayerAppearances)
  }
  def appearances(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String)(implicit context: ExecutionContext): Future[PlayerAppearances] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/appearances/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId").map(interceptErrors).map(parsePlayerAppearances)
  }
  def appearances(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String, competitionId: String)(implicit context: ExecutionContext): Future[PlayerAppearances] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/appearances/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId/$competitionId").map(interceptErrors).map(parsePlayerAppearances)
  }

  def playerStats(playerId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/stats/summary/$apiKey/$playerId/$startDateStr/$endDateStr").map(interceptErrors).map(parseStatsSummary)
  }
  def playerStats(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/stats/summary/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId").map(interceptErrors).map(parseStatsSummary)
  }
  def playerStats(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String, competitionId: String)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/player/stats/summary/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId/$competitionId").map(interceptErrors).map(parseStatsSummary)
  }

  def teamStats(teamId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/team/stats/summary/$apiKey/$teamId/$startDateStr/$endDateStr").map(interceptErrors).map(parseStatsSummary)
  }
  def teamStats(teamId: String, startDate: LocalDate, endDate: LocalDate, competitionId: String)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/api/football/team/stats/summary/$apiKey/$teamId/$startDateStr/$endDateStr/$competitionId").map(interceptErrors).map(parseStatsSummary)
  }

  def playerProfile(playerId: String)(implicit context: ExecutionContext): Future[PlayerProfile] = {
    get(s"/api/football/player/profile/$apiKey/$playerId").map(interceptErrors).map(parsePlayerProfile)
  }

  protected def get(suffix: String)(implicit context: ExecutionContext): Future[String] = GET(base + suffix).map{
    case Response(200, body, _) =>  body
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }

  /*
  PA API returns errors in 200 response with <errors> as first child element
  "...Errors are always returned as the first child element of the route element for the XML method in question..."
  http://pads6.pa-sport.com/api/football/default.aspx
   */
  protected def interceptErrors(xml: String): String = {
    val errors =  parseErrors(xml)
    if (errors.isEmpty) xml else throw PaClientErrorsException(errors.map(_.message).mkString(", "))
  }

  private def formatDates(startDate: LocalDate, endDate: LocalDate): (String, String) = (
    startDate.toString("yyyyMMdd"),
    endDate.toString("yyyyMMdd")
  )
}

class PaClientException(msg: String) extends RuntimeException(msg)
case class PaClientErrorsException(msg: String) extends RuntimeException(msg)
