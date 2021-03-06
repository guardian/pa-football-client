package pa

import java.time.{LocalDateTime, ZoneId}

import org.scalatest.{FlatSpec, Matchers, OptionValues}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class LiveMatchTest extends FlatSpec with Matchers with OptionValues {

  "PaClient" should "load live matches" in {
    val matches = Await.result(StubClient.liveMatches("100"), 10.seconds)

    matches.size should be (3)

    val liveMatch = matches(0)
    liveMatch should have (
      'id ("3528311"),
      'date (LocalDateTime.of(2012, 10, 7, 13, 30, 0, 0).atZone(ZoneId.of("Europe/London"))),
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

    try {
      Await.result(StubClient.liveMatches("108"), 10.seconds)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }

  }
}
