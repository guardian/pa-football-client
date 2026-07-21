package pa


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.OptionValues

class EventsTest extends AnyFlatSpec with Matchers with OptionValues {
  it should "load a match and match events" in {
    val theMatch = Await.result(StubClient.matchEvents("3888465"), 10.seconds).get

    theMatch.homeTeam should be (Team("999", "Spain"))
    theMatch.awayTeam should be (Team("6318", "Czech Republic"))

    theMatch.homeTeamScore should be (1)

    theMatch.awayTeamScore should be (0)

    theMatch.homeTeamGoals(0).players(0).name should be ("Gerard Pique")

    theMatch.isResult should be (true)

    val event = theMatch.events.find(_.id == Some("22306998")).value

    event should have(
      Symbol("matchTime") (Some("(90 +2:05)")),
      Symbol("eventTime") (Some("90")),
      Symbol("addedTime") (Some("2:05"))
    )
  }

  it should "parse the optional deleted flag on match events" in {
    val theMatch = Await.result(StubClient.matchEvents("3888465"), 10.seconds).get

    def event(id: String) = theMatch.events.find(_.id == Some(id)).value

    // deleted="true" -> Some(true)
    event("22306741").deleted should be (Some(true))

    // deleted="false" -> Some(false)
    event("22306742").deleted should be (Some(false))

    // attribute missing -> None
    event("22306998").deleted should be (None)
  }
}
