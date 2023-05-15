package pa

import java.time.{LocalDateTime, ZoneId}

import org.scalatest.OptionValues

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LiveMatchTest extends AnyFlatSpec with Matchers with OptionValues {

  "PaClient" should "load live matches" in {
    val matches = Await.result(StubClient.liveMatches("100"), 10.seconds)

    matches.size should be (3)

    val liveMatch = matches(0)
    liveMatch should have (
      Symbol("id") ("3528311"),
      Symbol("date") (LocalDateTime.of(2012, 10, 7, 13, 30, 0, 0).atZone(ZoneId.of("Europe/London"))),
      Symbol("stage") (Stage("1")),
      Symbol("round") (Round("1", None)),
      Symbol("leg") ("1"),
      Symbol("attendance") (None),
      Symbol("status") ("SHS"),
      Symbol("comments") (None)
    )
    liveMatch.homeTeam should have (
      Symbol("id") ("18"),
      Symbol("name") ("Southampton"),
      Symbol("score") (Some(1)),
      Symbol("htScore") (Some(1)),
      Symbol("aggregateScore") (Some(1)),
      Symbol("scorers") (Some("Jose Fonte (4)"))
    )
    liveMatch.awayTeam should have (
      Symbol("id") ("55"),
      Symbol("name") ("Fulham"),
      Symbol("score") (Some(1)),
      Symbol("htScore") (Some(0)),
      Symbol("aggregateScore") (None),
      Symbol("scorers") (Some("Jos Hooiveld (70 o.g.)"))
    )
    liveMatch.venue.value should have (
      Symbol("id") ("78"),
      Symbol("name") ("St. Mary's Stadium")
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
