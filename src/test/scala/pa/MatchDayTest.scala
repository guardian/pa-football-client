package pa

import org.scalatest.{OptionValues, FlatSpec, Matchers}
import org.joda.time.{DateTime, LocalDate}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MatchDayTest extends FlatSpec with Matchers with OptionValues {

  "PaClient" should "load a match day" in {
    val matches = Await.result(StubClient.matchDay("100", new LocalDate(2014, 8, 23)), 10.seconds)

    matches.size should be (6)
    val matchDay = matches(5)
    
    matchDay should have(
      'id ("3733674"),
      'date (new DateTime(2014, 8, 23, 17, 30, 0, 0)),
      'competition (None),
      'stage (Stage("1")),
      'round (Round("1", Some("League"))),
      'leg ("1"),
      'liveMatch (false),
      'result (true),
      'previewAvailable (true),
      'reportAvailable (true),
      'lineupsAvailable (true),
      'matchStatus ("FT"),
      'attendance (Some("39490")),
      'comments (None)
    )
    matchDay.homeTeam should have (
      'id ("8"),
      'name ("Everton"),
      'score (Some(2)),
      'htScore (Some(2)),
      'aggregateScore (None),
      'scorers (Some("Seamus Coleman (19),Steven Naismith (45)"))
    )
    matchDay.awayTeam should have (
      'id ("1006"),
      'name ("Arsenal"),
      'score (Some(2)),
      'htScore (Some(0)),
      'aggregateScore (None),
      'scorers (Some("Aaron Ramsey (83),Olivier Giroud (90)"))
    )
    matchDay.referee.value should have (
      'id ("233470"),
      'name ("Kevin Friend")
    )
    matchDay.venue.value should have (
      'id ("71"),
      'name ("Goodison Park")
    )
  }

  it should "parse a day that only has a single game" in {

    val matches = Await.result(StubClient.matchDay("100", new LocalDate(2014, 8, 25)), 10.seconds)

    matches.size should be (1)

    matches(0).id should be ("3744626")
  }

  it should "handle days with no data" in {
    try {
      Await.result(StubClient.matchDay("100", new LocalDate(2014, 8, 1)), 10.seconds)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }
  }

  it should "parse match with missing referee" in {

    val matches = Await.result(StubClient.matchDay("750", new LocalDate(2016, 6, 25)), 10.seconds)

    matches(0).referee should be (None)
  }

  it should "parse match with complex round/stage" in {

    val matches = Await.result(StubClient.matchDay("101", new LocalDate(2016, 5, 28)), 10.seconds)

    val matchDay = matches(0)
    matchDay.round should be (Round("2", Some("Play-Offs Final")))
    matchDay.stage should be (Stage("2"))
    matchDay.leg should be ("1")
  }

  it should "parse match without half time score" in {

    val matches = Await.result(StubClient.matchDay("750", new LocalDate(2016, 6, 25)), 10.seconds)

    matches(0).awayTeam.htScore should be (None)
    matches(0).awayTeam.aggregateScore should be (None)

  }

  it should "parse match without attendance" in {

    val matches = Await.result(StubClient.matchDay("750", new LocalDate(2016, 6, 25)), 10.seconds)

    matches(0).attendance should be (None)
  }


  it should "parse match without score" in {

    val matches = Await.result(StubClient.matchDay("750", new LocalDate(2016, 6, 25)), 10.seconds)

    matches(0).homeTeam.score should be (None)
  }
  
  it should "load all match days" in {

    val matches = Await.result(StubClient.matchDay(new LocalDate(2014, 9, 29)), 10.seconds)
    
    matches.size should be (5)

    matches(0).competition should be (Some(Competition("100", "Barclays Premier League 14/15")))

  }
    
}
