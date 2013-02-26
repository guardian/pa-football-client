package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}

class MatchDayTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load a match day" in {
    val matches = StubClient.matchDay("100", new DateMidnight(2011, 8, 27))

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

  "PaClient" should "load a match by ID" in {
    val _match = StubClient.matchDay("3560717")

    _match.isEmpty should be (false)

    val matchDay = _match.get

    matchDay.id should be ("3560717")
    matchDay.date should be (new DateTime(2012, 10, 31, 19, 45, 0, 0))
    matchDay.competition should be (None)
    matchDay.round should be (Some(Round("5", Some("Fourth Round"))))
    matchDay.leg should be ("1")
    matchDay.liveMatch should be (false)
    matchDay.result should be (true)
    matchDay.previewAvailable should be (true)
    matchDay.lineupsAvailable should be (true)
    matchDay.matchStatus should be ("FT")
    matchDay.attendance should be (Some("41126"))
    matchDay.homeTeam should be (MatchDayTeam(
      "4",
      "Chelsea",
      Some(5),
      Some(1),
      None,
      Some("David Luiz (31 Pen),Gary Cahill (52),Eden Hazard (90 +3:53 Pen),Daniel Sturridge (97),Nascimento Ramires (116)")
      ))
    matchDay.awayTeam should be (MatchDayTeam(
      "12",
      "Man Utd",
      Some(4),
      Some(2),
      None,
      Some("Ryan Giggs (22),Javier Hernandez (43),Luis Nani (59),Ryan Giggs (120 Pen)")
    ))
    matchDay.referee should be (Some(Official("182780", "Lee Mason")))
    matchDay.venue should be (Some(Venue("511", "Stamford Bridge")))
    matchDay.comments should be (Some("(After Extra Time)"))
  }

  it should "load comments" in {
    val theMatch = StubClient.matchDay("100", new DateMidnight(2011, 3, 19)).find(_.id == "3284716").get

    theMatch.comments should be  (Some("Wolverhampton win in extra time"))
  }

  it should "parse a day that only has a single game" in {

    val matches = StubClient.matchDay("100", new DateMidnight(2010, 8, 15))

    matches.size should be (1)

    matches(0).id should be ("3293494")
  }

  it should "handle days with no data" in {

    val matches = StubClient.matchDay("100", new DateMidnight(2010, 8, 1))

    matches.size should be (0)
  }

  it should "parse match with missing venue" in {

    val matches = StubClient.matchDay("100", new DateMidnight(2011, 3, 19))

    matches(0).venue should be (None)
  }

  it should "parse match with missing referee" in {

    val matches = StubClient.matchDay("101", new DateMidnight(2010, 11, 07))

    matches(0).referee should be (None)
  }

  it should "parse match with round" in {

    val matches = StubClient.matchDay("101", new DateMidnight(2011, 5, 16))

    matches(0).round should be (Some(Round("1", Some("Play-Offs Semi-Final"))))
  }

  it should "parse match without half time score" in {

    val matches = StubClient.matchDay("102", new DateMidnight(2010, 11, 02))

    matches(4).awayTeam.htScore should be (None)
    matches(4).awayTeam.aggregateScore should be (None)

  }

  it should "parse match without attendance" in {

    val matches = StubClient.matchDay("102", new DateMidnight(2010, 11, 02))

    matches(4).attendance should be (None)
  }


  it should "parse match without score" in {

    val matches = StubClient.matchDay("794", new DateMidnight(2011, 4, 16))

    matches(0).homeTeam.score should be (None)
  }
  
  it should "load all match days" in {

    val matches = StubClient.matchDay(new DateMidnight(2012, 9, 29))
    
    matches.size should be (2)

    matches(0).competition should be (Some(Competition("100", "Barclays Premier League 12/13")))

  }
    
}
