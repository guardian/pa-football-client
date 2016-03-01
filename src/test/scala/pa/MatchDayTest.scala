package pa

import org.scalatest.{OptionValues, FlatSpec, ShouldMatchers}
import org.joda.time.{DateTime, LocalDate}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MatchDayTest extends FlatSpec with ShouldMatchers with OptionValues {

  "PaClient" should "load a match day" in {
    val matches = Await.result(StubClient.matchDay("100", new LocalDate(2011, 8, 27)), 1.second)

    matches.size should be (6)
    val matchDay = matches(3)
    
    matchDay should have(
      'id ("3407349"),
      'date (new DateTime(2011, 8, 27, 15, 0, 0, 0)),
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
      'attendance (Some("41765")),
      'comments (None)
    )
    matchDay.homeTeam should have (
      'id ("4"),
      'name ("Chelsea"),
      'score (Some(3)),
      'htScore (Some(1)),
      'aggregateScore (None),
      'scorers (Some("Jose Bosingwa (6),Frank Lampard (82 Pen),Juan Mata (90 +10:03)"))
    )
    matchDay.awayTeam should have (
      'id ("14"),
      'name ("Norwich"),
      'score (Some(1)),
      'htScore (Some(0)),
      'aggregateScore (None),
      'scorers (Some("Grant Holt (63)"))
    )
    matchDay.referee.value should have (
      'id ("410888"),
      'name ("Mike Jones")
    )
    matchDay.venue.value should have (
      'id ("511"),
      'name ("Stamford Bridge")
    )
  }

  it should "load comments" in {
    val theMatch = Await.result(StubClient.matchDay("100", new LocalDate(2011, 3, 19)), 1.second).find(_.id == "3284716").get

    theMatch.comments should be  (Some("Wolverhampton win in extra time"))
  }

  it should "parse a day that only has a single game" in {

    val matches = Await.result(StubClient.matchDay("100", new LocalDate(2010, 8, 15)), 1.second)

    matches.size should be (1)

    matches(0).id should be ("3293494")
  }

  it should "handle days with no data" in {
    try {
      Await.result(StubClient.matchDay("100", new LocalDate(2010, 8, 1)), 1.second)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }
  }

  it should "parse match with missing venue" in {

    val matches = Await.result(StubClient.matchDay("100", new LocalDate(2011, 3, 19)), 1.second)

    matches(0).venue should be (None)
  }

  it should "parse match with missing referee" in {

    val matches = Await.result(StubClient.matchDay("101", new LocalDate(2010, 11, 7)), 1.second)

    matches(0).referee should be (None)
  }

  it should "parse match with complex round/stage" in {

    val matches = Await.result(StubClient.matchDay("101", new LocalDate(2011, 5, 16)), 1.second)

    val matchDay = matches(0)
    matchDay.round should be (Round("1", Some("Play-Offs Semi-Final")))
    matchDay.stage should be (Stage("2"))
    matchDay.leg should be ("2")
  }

  it should "parse match without half time score" in {

    val matches = Await.result(StubClient.matchDay("102", new LocalDate(2010, 11, 2)), 1.second)

    matches(4).awayTeam.htScore should be (None)
    matches(4).awayTeam.aggregateScore should be (None)

  }

  it should "parse match without attendance" in {

    val matches = Await.result(StubClient.matchDay("102", new LocalDate(2010, 11, 2)), 1.second)

    matches(4).attendance should be (None)
  }


  it should "parse match without score" in {

    val matches = Await.result(StubClient.matchDay("794", new LocalDate(2011, 4, 16)), 1.second)

    matches(0).homeTeam.score should be (None)
  }
  
  it should "load all match days" in {

    val matches = Await.result(StubClient.matchDay(new LocalDate(2012, 9, 29)), 1.second)
    
    matches.size should be (2)

    matches(0).competition should be (Some(Competition("100", "Barclays Premier League 12/13")))

  }
    
}
