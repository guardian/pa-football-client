package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class LiveMatchTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load live matches" in {
    val matches = Await.result(StubClient.liveMatches("100"), 1.second)

    matches.size should be (3)

    matches(0) should be(
      LiveMatch(
        "3528311",
        new DateTime(2012, 10, 7, 13, 30, 0,0),
        Some(Round("1", None)),
        None,
        MatchDayTeam("18", "Southampton", Some(1), Some(1), Some(1), Some("Jose Fonte (4)")),
        MatchDayTeam("55", "Fulham", Some(1), Some(0), None, Some("Jos Hooiveld (70 o.g.)")),
        Some(Official("186005", "Mark Clattenburg")),
        Some(Venue("78", "St. Mary's Stadium")),
        status = "SHS",
        None
      )
    )
  }

  it should "be empty if there are no matches" in {

    Await.result(StubClient.liveMatches("108"), 1.second).size should be (0)

  }
}
