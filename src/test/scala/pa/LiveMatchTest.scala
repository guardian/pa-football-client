package pa

import org.scalatest.{OptionValues, FlatSpec, ShouldMatchers}
import org.joda.time.{DateTime, LocalDate}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class LiveMatchTest extends FlatSpec with ShouldMatchers with OptionValues {

  "PaClient" should "load live matches" in {
    val matches = Await.result(StubClient.liveMatches("100"), 1.second)

    matches.size should be (3)

    val liveMatch = matches(0)
    liveMatch should have (
      'id ("3528311"),
      'date (new DateTime(2012, 10, 7, 13, 30, 0,0)),
      'stage (Stage("1")),
      'round (Round("1", None)),
      'leg ("1"),
      'attendance (None),
      'status ("SHS"),
      'comments (None)
    )
    liveMatch.homeTeam should have (
      'id ("18"),
      'name ("Southampton"),
      'score (Some(1)),
      'htScore (Some(1)),
      'aggregateScore (Some(1)),
      'scorers (Some("Jose Fonte (4)"))
    )
    liveMatch.awayTeam should have (
      'id ("55"),
      'name ("Fulham"),
      'score (Some(1)),
      'htScore (Some(0)),
      'aggregateScore (None),
      'scorers (Some("Jos Hooiveld (70 o.g.)"))
    )
    liveMatch.venue.value should have (
      'id ("78"),
      'name ("St. Mary's Stadium")
    )
  }

  it should "be empty if there are no matches" in {

    Await.result(StubClient.liveMatches("108"), 1.second).size should be (0)

  }
}
