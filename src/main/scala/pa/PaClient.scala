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

  def results(competitionId: String, start: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] = results(competitionId, start, None)

  def results(competitionId: String, start: DateMidnight, end: DateMidnight)(implicit context: ExecutionContext): Future[List[Result]] =
    results(competitionId, start, Some(end))

  private def results(competitionId: String, start: DateMidnight, end: Option[DateMidnight] = None)(implicit context: ExecutionContext): Future[List[Result]] ={
    val dateStr = start.toString("yyyyMMdd") + (end map { e => s"/${e.toString("yyyyMMdd")}" } getOrElse "")
    get(s"/api/football/competition/results/$apiKey/$competitionId/$dateStr").map(parseResults)
  }

  def leagueTable(competitionId: String, date: DateMidnight)(implicit context: ExecutionContext): Future[List[LeagueTableEntry]] =
      get(s"/api/football/competition/leagueTable/$apiKey/$competitionId/${date.toString("yyyyMMdd")}").map(parseLeagueTable)


  def fixtures(implicit context: ExecutionContext): Future[List[Fixture]] = get(s"/api/football/competitions/fixtures/$apiKey").map(parseFixtures)
  
  def fixtures(competitionId: String)(implicit context: ExecutionContext): Future[List[Fixture]] =
    get(s"/api/football/competition/fixtures/$apiKey/$competitionId").map(parseFixtures)

  def liveMatches(competitionId: String)(implicit context: ExecutionContext): Future[List[LiveMatch]] =
    get(s"/api/football/competition/liveGames/$apiKey/$competitionId").map(parseLiveMatches)


  protected def get(suffix: String)(implicit context: ExecutionContext): Future[String] = GET(base + suffix).map{
    case Response(200, body, _) =>  body
    case Response(status, _, reason) => throw new PaClientException(status + " " + reason)
  }
}

class PaClientException(msg: String) extends RuntimeException(msg)
