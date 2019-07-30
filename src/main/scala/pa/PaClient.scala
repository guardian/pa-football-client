package pa


import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import Parser._

import concurrent.{ExecutionContext, Future}

object PaClientConfig {
  lazy val baseUrl: String = "https://football.api.press.net/v1.5"
}

trait PaClient { self: Http =>

  val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

  def apiKey: String

  lazy val base: String = PaClientConfig.baseUrl

  def competitions(implicit context: ExecutionContext): Future[List[Season]] =
    get(s"/competitions/competitions/$apiKey").map(interceptErrors).map(parseCompetitions)

  def matchInfo(id: String)(implicit context: ExecutionContext): Future[MatchDay] =
    get(s"/match/info/$apiKey/$id").map(interceptErrors).map(parseMatchInfo)

  def matchEvents(id: String)(implicit context: ExecutionContext): Future[Option[MatchEvents]] =
    get(s"/match/events/$apiKey/$id").map(interceptErrors).map(parseMatchEvents)

  def matchStats(id: String)(implicit context: ExecutionContext): Future[Seq[MatchStats]] =
    get(s"/match/stats/$apiKey/$id").map(interceptErrors).map(parseMatchStats)

  def lineUp(id: String)(implicit context: ExecutionContext): Future[LineUp] = get(s"/match/lineUps/$apiKey/$id").map(interceptErrors).map(parseLineUp)

  def matchDay(date: LocalDate)(implicit context: ExecutionContext): Future[List[MatchDay]] =
    get(s"/competitions/matchDay/$apiKey/${date.format(formatter)}").map(interceptErrors).map(parseMatchDay)
  def matchDay(competitionId: String, date: LocalDate)(implicit context: ExecutionContext): Future[List[MatchDay]] =
    get(s"/competition/matchDay/$apiKey/$competitionId/${date.format(formatter)}").map(interceptErrors).map(parseMatchDay)

  def results(start: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] = resultsAll(start, None)
  def results(start: LocalDate, end: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] = resultsAll(start, Some(end))

  private def resultsAll(start: LocalDate, end: Option[LocalDate] = None)(implicit context: ExecutionContext): Future[List[Result]] = {
    val dateStr = start.format(formatter) + (end map { e => s"/${e.format(formatter)}"} getOrElse "")
    get(s"/competitions/results/$apiKey/$dateStr").map(interceptErrors).map(parseResults)
  }

  def results(competitionId: String, start: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] = results("competition", competitionId, start, None)
  def results(competitionId: String, start: LocalDate, end: LocalDateTime)(implicit context: ExecutionContext): Future[List[Result]] =
    results("competition", competitionId, start, Some(end))

  def teamResults(teamId: String, start: LocalDate)(implicit context: ExecutionContext): Future[List[Result]] =
    results("team", teamId, start, None)
  def teamResults(teamId: String, start: LocalDate, end: LocalDateTime)(implicit context: ExecutionContext): Future[List[Result]] =
    results("team", teamId, start, Some(end))

  private def results(resultType: String, competitionId: String, start: LocalDate, end: Option[LocalDateTime] = None)(implicit context: ExecutionContext): Future[List[Result]] ={
    val dateStr = start.format(formatter) + (end map { e => s"/${e.format(formatter)}" } getOrElse "")
    get(s"/$resultType/results/$apiKey/$competitionId/$dateStr").map(interceptErrors).map(parseResults)
  }

  def leagueTable(competitionId: String, date: LocalDate)(implicit context: ExecutionContext): Future[List[LeagueTableEntry]] =
      get(s"/competition/leagueTable/$apiKey/$competitionId/${date.format(formatter)}").map(interceptErrors).map(parseLeagueTable)


  def fixtures(implicit context: ExecutionContext): Future[List[Fixture]] = get(s"/competitions/fixtures/$apiKey").map(interceptErrors).map(parseFixtures)
  def fixtures(competitionId: String)(implicit context: ExecutionContext): Future[List[Fixture]] =
    get(s"/competition/fixtures/$apiKey/$competitionId").map(interceptErrors).map(parseFixtures)

  def liveMatches(competitionId: String)(implicit context: ExecutionContext): Future[List[LiveMatch]] =
    get(s"/competition/liveGames/$apiKey/$competitionId").map(interceptErrors).map(parseLiveMatches)

  def teamHead2Head(team1Id: String, team2Id: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/team/headToHeads/$apiKey/$team1Id/$team2Id/$startDateStr/$endDateStr").map(interceptErrors).map(parseTeamHead2Head)
  }
  def teamHead2Head(team1Id: String, team2Id: String, startDate: LocalDate, endDate: LocalDate, competitionId: String)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/team/headToHeads/$apiKey/$team1Id/$team2Id/$startDateStr/$endDateStr/$competitionId").map(interceptErrors).map(parseTeamHead2Head)
  }

  def teamEvents(teamId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[List[TeamEventMatch]] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/team/events/$apiKey/$teamId/$startDateStr/$endDateStr").map(interceptErrors).map(parseTeamEventMatches)
  }

  def squad(teamId: String)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    get(s"/team/squad/$apiKey/$teamId").map(interceptErrors).map(parseSquad)
  }
  def squad(teamId: String, startDate: LocalDate)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    val startDateStr = startDate.format(formatter)
    get(s"/team/squad/$apiKey/$teamId/$startDateStr").map(interceptErrors).map(parseSquad)
  }
  def squad(teamId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/team/squad/$apiKey/$teamId/$startDateStr/$endDateStr").map(interceptErrors).map(parseSquad)
  }

  def teams(competitionId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[List[Team]] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/competition/teams/$apiKey/$competitionId/$startDateStr/$endDateStr").map(interceptErrors).map(parseTeams)
  }

  def playerHead2Head(player1Id: String, player2Id: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/headToHeads/$apiKey/$player1Id/$player2Id/$startDateStr/$endDateStr").map(interceptErrors).map(parsePlayerHead2Head)
  }
  def playerHead2Head(player1Id: String, player2Id: String, startDate: LocalDate, endDate: LocalDate, competitionId: String)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/headToHeads/$apiKey/$player1Id/$player2Id/$startDateStr/$endDateStr/$competitionId").map(interceptErrors).map(parsePlayerHead2Head)
  }

  def appearances(playerId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[PlayerAppearances] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/appearances/$apiKey/$playerId/$startDateStr/$endDateStr").map(interceptErrors).map(parsePlayerAppearances)
  }
  def appearances(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String)(implicit context: ExecutionContext): Future[PlayerAppearances] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/appearances/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId").map(interceptErrors).map(parsePlayerAppearances)
  }
  def appearances(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String, competitionId: String)(implicit context: ExecutionContext): Future[PlayerAppearances] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/appearances/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId/$competitionId").map(interceptErrors).map(parsePlayerAppearances)
  }

  def playerStats(playerId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/stats/summary/$apiKey/$playerId/$startDateStr/$endDateStr").map(interceptErrors).map(parseStatsSummary)
  }
  def playerStats(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/stats/summary/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId").map(interceptErrors).map(parseStatsSummary)
  }
  def playerStats(playerId: String, startDate: LocalDate, endDate: LocalDate, teamId: String, competitionId: String)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/player/stats/summary/$apiKey/$playerId/$startDateStr/$endDateStr/$teamId/$competitionId").map(interceptErrors).map(parseStatsSummary)
  }

  def teamStats(teamId: String, startDate: LocalDate, endDate: LocalDate)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/team/stats/summary/$apiKey/$teamId/$startDateStr/$endDateStr").map(interceptErrors).map(parseStatsSummary)
  }
  def teamStats(teamId: String, startDate: LocalDate, endDate: LocalDate, competitionId: String)(implicit context: ExecutionContext): Future[StatsSummary] = {
    val (startDateStr, endDateStr) = formatDates(startDate, endDate)
    get(s"/team/stats/summary/$apiKey/$teamId/$startDateStr/$endDateStr/$competitionId").map(interceptErrors).map(parseStatsSummary)
  }

  def playerProfile(playerId: String)(implicit context: ExecutionContext): Future[PlayerProfile] = {
    get(s"/player/profile/$apiKey/$playerId").map(interceptErrors).map(parsePlayerProfile)
  }

  protected def get(suffix: String)(implicit context: ExecutionContext): Future[String] = GET(base + suffix).map{
    case Response(200, body, _) =>  body
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }

  /*
   * PA API returns errors in 200 response with <errors> as first child element
   * "...Errors are always returned as the first child element of the route element for the XML method in question..."
   */
  protected def interceptErrors(xml: String): String = {
    val errors =  parseErrors(xml)
    if (errors.isEmpty) xml else throw PaClientErrorsException(errors.map(_.message).mkString(", "))
  }

  private def formatDates(startDate: LocalDate, endDate: LocalDate): (String, String) = (
    startDate.format(formatter),
    endDate.format(formatter)
  )
}

class PaClientException(msg: String) extends RuntimeException(msg)
case class PaClientErrorsException(msg: String) extends RuntimeException(msg)
