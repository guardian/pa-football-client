package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}

class ResultsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load results" in {
    val matches = StubClient.results("100", new DateMidnight(2010, 8, 1))

    matches.size should be(780)

    matches(3) should be(
      Result(
        "3517772",
        new DateTime(2012, 8, 25, 15, 0, 0, 0),
        Some(Round("1", None)),
        "1",
        reportAvailable = true,
        attendance = Some("36166"),
        homeTeam = MatchDayTeam(
          "19", "Tottenham", Some(1), Some(0), None, Some("Benoit Assou-Ekotto (74)")
        ),
        awayTeam = MatchDayTeam("42", "West Brom", Some(1), Some(0), None, Some("James Morrison (90 +0:21)")),
        referee = Some(Official("100924", "Mike Dean")),
        venue = Some(Venue("61", "White Hart Lane"))
      )
    )
  }

  it should "work with an end date" in {
    val matches = StubClient.results("100", new DateMidnight(2012, 8, 23), new DateMidnight(2012, 9, 1))

    matches(0).homeTeam.name should be ("Liverpool")
  }
}
