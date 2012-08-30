package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}

class ResultsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load results" in {
    val matches = StubClient.results("100", new DateMidnight(2011, 8, 27))

    matches.size should be(6)

    matches(3) should be(
      Result(
        "3407349",
        new DateTime(2011, 8, 27, 15, 0, 0, 0),
        Some(Round("1", None)),
        Some("1"),
        reportAvailable = true,
        attendance = Some("41765"),
        homeTeam = MatchDayTeam(
          "4", Some("Chelsea"), Some(3), Some(1), None, Some("Jose Bosingwa (6),Frank Lampard (82 Pen),Juan Mata (90 +10:03)")
        ),
        awayTeam = MatchDayTeam("14", Some("Norwich"), Some(1), Some(0), None, Some("Grant Holt (63)")),
        referee = Some(Official("410888", "Mike Jones")),
        venue = Some(Venue("511", "Stamford Bridge"))
      )
    )
  }

  it should "parse a single result" in {

    val matches = StubClient.results("100", new DateMidnight(2010, 8, 15))

    matches.size should be(1)

    matches(0).id should be("3293494")
  }

  it should "handle no results" in {

    val matches = StubClient.results("100", new DateMidnight(2010, 8, 1))

    matches.size should be(0)
  }

  it should "parse results with missing venue" in {

    val matches = StubClient.results("100", new DateMidnight(2011, 3, 19))

    matches(0).venue should be(None)
  }

  it should "parse results with missing referee" in {

    val matches = StubClient.results("101", new DateMidnight(2010, 11, 07))

    matches(0).referee should be(None)
  }

  it should "parse results with round" in {

    val matches = StubClient.results("101", new DateMidnight(2011, 5, 16))

    matches(0).round should be(Some(Round("1", Some("Play-Offs Semi-Final"))))
  }

  it should "parse results without half time score" in {

    val matches = StubClient.results("102", new DateMidnight(2010, 11, 02))

    matches(4).awayTeam.htScore should be(None)
    matches(4).awayTeam.aggregateScore should be(None)

  }

  it should "parse results without attendance" in {

    val matches = StubClient.results("102", new DateMidnight(2010, 11, 02))

    matches(4).attendance should be(None)
  }


  it should "parse results without score" in {

    val matches = StubClient.results("794", new DateMidnight(2011, 4, 16))

    matches(0).homeTeam.score should be(None)
  }

  it should "parse results when a team has no name" in {
    val matches = StubClient.results("100", new DateMidnight(2012, 8, 23), Some(new DateMidnight(2012, 9, 1)))

    matches(0).homeTeam.name should be (None)
  }
}
