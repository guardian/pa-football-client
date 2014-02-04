package pa


import Parser._
import org.joda.time.DateMidnight
import concurrent.{ExecutionContext, Future, Promise}

trait PaClient { self: Http =>

  def apiKey: String

  lazy val base: String = "http://pads6.pa-sport.com"

  def competitions(implicit context: ExecutionContext): Future[List[Season]] =
    get(s"/api/football/competitions/competitions/$apiKey").map(parseCompetitions)

  def matchEvents(id: String)(implicit context: ExecutionContext): Future[Option[MatchEvents]] =
    get(s"/api/football/match/events/$apiKey/$id").map(parseMatchEvents)

  def matchStats(id: String)(implicit context: ExecutionContext): Future[Seq[MatchStats]] =
    get(s"/api/football/match/stats/$apiKey/$id").map(parseMatchStats)

  def lineUp(id: String)(implicit context: ExecutionContext): Future[LineUp] = get(s"/api/football/match/lineUps/$apiKey/$id").map(parseLineUp)

  def matchDay(date: DateMidnight)(implicit context: ExecutionContext): Future[List[MatchDay]] =
    get(s"/api/football/competitions/matchDay/$apiKey/${date.toString("yyyyMMdd")}").map(parseMatchDay)

    
  def matchDay(competitionId: String, date: DateMidnight)(implicit context: ExecutionContext): Future[List[MatchDay]] =
    get(s"/api/football/competition/matchDay/$apiKey/$competitionId/${date.toString("yyyyMMdd")}").map(parseMatchDay)

  def results(start: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] = resultsAll(start, None)

  def results(start: DateMidnight, end: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] = resultsAll(start, Some(end))

  private def resultsAll(start: DateMidnight, end: Option[DateMidnight] = None)(implicit context: ExecutionContext): Future[List[Result]] = {
    val dateStr = start.toString("yyyyMMdd") + (end map { e => s"/${e.toString("yyyyMMdd")}"} getOrElse "")
    get(s"/api/football/competitions/results/$apiKey/$dateStr").map(parseResults)
  }

  def results(competitionId: String, start: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] = results("competition", competitionId, start, None)

  def results(competitionId: String, start: DateMidnight, end: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] =
    results("competition", competitionId, start, Some(end))

  def teamResults(teamId: String, start: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] =
    results("team", teamId, start, None)

  def teamResults(teamId: String, start: DateMidnight, end: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] =
    results("team", teamId, start, Some(end))

  private def results(resultType: String, competitionId: String, start: DateMidnight, end: Option[DateMidnight] = None)(implicit context: ExecutionContext): Future[List[Result]] ={
    val dateStr = start.toString("yyyyMMdd") + (end map { e => s"/${e.toString("yyyyMMdd")}" } getOrElse "")
    get(s"/api/football/$resultType/results/$apiKey/$competitionId/$dateStr").map(parseResults)
  }

  def leagueTable(competitionId: String, date: DateMidnight)(implicit context: ExecutionContext): Future[List[LeagueTableEntry]] =
      get(s"/api/football/competition/leagueTable/$apiKey/$competitionId/${date.toString("yyyyMMdd")}").map(parseLeagueTable)


  def fixtures(implicit context: ExecutionContext): Future[List[Fixture]] = get(s"/api/football/competitions/fixtures/$apiKey").map(parseFixtures)
  
  def fixtures(competitionId: String)(implicit context: ExecutionContext): Future[List[Fixture]] =
    get(s"/api/football/competition/fixtures/$apiKey/$competitionId").map(parseFixtures)

  def liveMatches(competitionId: String)(implicit context: ExecutionContext): Future[List[LiveMatch]] =
    get(s"/api/football/competition/liveGames/$apiKey/$competitionId").map(parseLiveMatches)

  def teamHead2Head(team1Id: String, team2Id: String, startDate: DateMidnight, endDate: DateMidnight)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] =
    teamHead2Head(team1Id, team2Id, startDate, endDate, None)

  def teamHead2Head(team1Id: String, team2Id: String, startDate: DateMidnight, endDate: DateMidnight, competitionId: String)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] =
    teamHead2Head(team1Id, team2Id, startDate, endDate, Some(competitionId))

  private def teamHead2Head(team1Id: String, team2Id: String, startDate: DateMidnight, endDate: DateMidnight, competitionId: Option[String])(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    val endDateStr = endDate.toString("yyyyMMdd")
    val competitionIdStr = competitionId.map(id => s"/$id").getOrElse("")
    get(s"/api/football/team/headToHeads/$apiKey/$team1Id/$team2Id/$startDateStr/$endDateStr$competitionIdStr").map(parseTeamHead2Head)
  }

  def teamEvents(teamId: String, startDate: DateMidnight, endDate: DateMidnight)(implicit context: ExecutionContext): Future[List[TeamEventMatch]] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    val endDateStr = endDate.toString("yyyyMMdd")
    get(s"/api/football/team/events/$apiKey/$teamId/$startDateStr/$endDateStr").map(parseTeamEventMatches)
  }

  def squad(teamId: String)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    get(s"/api/football/team/squad/$apiKey/$teamId").map(parseSquad)
  }
  def squad(teamId: String, startDate: DateMidnight)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    get(s"/api/football/team/squad/$apiKey/$teamId/$startDateStr").map(parseSquad)
  }
  def squad(teamId: String, startDate: DateMidnight, endDate: DateMidnight)(implicit context: ExecutionContext): Future[List[SquadMember]] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    val endDateStr = endDate.toString("yyyyMMdd")
    get(s"/api/football/team/squad/$apiKey/$teamId/$startDateStr/$endDateStr").map(parseSquad)
  }

  def teams(competitionId: String, startDate: DateMidnight, endDate: DateMidnight)(implicit context: ExecutionContext): Future[List[Team]] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    val endDateStr = endDate.toString("yyyyMMdd")
    get(s"/api/football/competition/teams/$apiKey/$competitionId/$startDateStr/$endDateStr").map(parseTeams)
  }

  def playerHead2Head(player1Id: String, player2Id: String, startDate: DateMidnight, endDate: DateMidnight)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] =
    playerHead2Head(player1Id, player2Id, startDate, endDate, None)

  def playerHead2Head(player1Id: String, player2Id: String, startDate: DateMidnight, endDate: DateMidnight, competitionId: String)(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] =
    playerHead2Head(player1Id, player2Id, startDate, endDate, Some(competitionId))

  private def playerHead2Head(player1Id: String, player2Id: String, startDate: DateMidnight, endDate: DateMidnight, competitionId: Option[String])(implicit context: ExecutionContext): Future[(Head2Head, Head2Head)] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    val endDateStr = endDate.toString("yyyyMMdd")
    val competitionIdStr = competitionId.map(id => s"/$id").getOrElse("")
    get(s"/api/football/player/headToHeads/$apiKey/$player1Id/$player2Id/$startDateStr/$endDateStr$competitionIdStr").map(parsePlayerHead2Head)
  }

  def appearances(playerId: String, startDate: DateMidnight, endDate: DateMidnight)(implicit context: ExecutionContext): Future[PlayerAppearances] = {
    val startDateStr = startDate.toString("yyyyMMdd")
    val endDateStr = endDate.toString("yyyyMMdd")
    get(s"/api/football/player/appearances/$apiKey/$playerId/$startDateStr/$endDateStr").map(parsePlayerAppearances)
  }

  protected def get(suffix: String)(implicit context: ExecutionContext): Future[String] = GET(base + suffix).map{
    case Response(200, body, _) =>  body
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }
}

class PaClientException(msg: String) extends RuntimeException(msg)
