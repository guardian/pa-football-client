package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MatchDayTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load a match day" in {
    val matches = Await.result(StubClient.matchDay("100", new DateMidnight(2011, 8, 27)), 1.second)

    matches.size should be (6)

    matches(3) should be(
      MatchDay(
        "3407349",
        new DateTime(2011, 8, 27, 15, 0, 0, 0),
        None,
        Some(Round("1", None)),
        "1",
        liveMatch = false,
        result = true,
        previewAvailable = true,
        reportAvailable = true,
        lineupsAvailable = true,
        matchStatus = "FT",
        attendance = Some("41765"),
        homeTeam = MatchDayTeam(
          "4", "Chelsea", Some(3), Some(1), None, Some("Jose Bosingwa (6),Frank Lampard (82 Pen),Juan Mata (90 +10:03)")
        ),
        awayTeam = MatchDayTeam("14", "Norwich", Some(1), Some(0), None, Some("Grant Holt (63)")),
        referee = Some(Official("410888", "Mike Jones")),
        venue = Some(Venue("511", "Stamford Bridge")),
        comments = None
      )
    )
  }

  it should "load comments" in {
    val theMatch = Await.result(StubClient.matchDay("100", new DateMidnight(2011, 3, 19)), 1.second).find(_.id == "3284716").get

    theMatch.comments should be  (Some("Wolverhampton win in extra time"))
  }

  it should "parse a day that only has a single game" in {

    val matches = Await.result(StubClient.matchDay("100", new DateMidnight(2010, 8, 15)), 1.second)

    matches.size should be (1)

    matches(0).id should be ("3293494")
  }

  it should "handle days with no data" in {

    val matches = Await.result(StubClient.matchDay("100", new DateMidnight(2010, 8, 1)), 1.second)

    matches.size should be (0)
  }

  it should "parse match with missing venue" in {

    val matches = Await.result(StubClient.matchDay("100", new DateMidnight(2011, 3, 19)), 1.second)

    matches(0).venue should be (None)
  }

  it should "parse match with missing referee" in {

    val matches = Await.result(StubClient.matchDay("101", new DateMidnight(2010, 11, 7)), 1.second)

    matches(0).referee should be (None)
  }

  it should "parse match with round" in {

    val matches = Await.result(StubClient.matchDay("101", new DateMidnight(2011, 5, 16)), 1.second)

    matches(0).round should be (Some(Round("1", Some("Play-Offs Semi-Final"))))
  }

  it should "parse match without half time score" in {

    val matches = Await.result(StubClient.matchDay("102", new DateMidnight(2010, 11, 2)), 1.second)

    matches(4).awayTeam.htScore should be (None)
    matches(4).awayTeam.aggregateScore should be (None)

  }

  it should "parse match without attendance" in {

    val matches = Await.result(StubClient.matchDay("102", new DateMidnight(2010, 11, 2)), 1.second)

    matches(4).attendance should be (None)
  }


  it should "parse match without score" in {

    val matches = Await.result(StubClient.matchDay("794", new DateMidnight(2011, 4, 16)), 1.second)

    matches(0).homeTeam.score should be (None)
  }
  
  it should "load all match days" in {

    val matches = Await.result(StubClient.matchDay(new DateMidnight(2012, 9, 29)), 1.second)
    
    matches.size should be (2)

    matches(0).competition should be (Some(Competition("100", "Barclays Premier League 12/13")))

  }
    
}
