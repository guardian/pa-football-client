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
  
  it should "parse the status flag on match events" in {
    val theMatch = Await.result(StubClient.matchEvents("3888465"), 10.seconds).get

    def event(id: String) = theMatch.events.find(_.id == Some(id)).value

    // status="active" -> active
    event("22306972").status should be (Some("active"))
    event("22306972").isDeleted should be(false)

    // status="deleted" -> deleted
    event("223069721").status should be (Some("deleted"))
    event("223069721").isDeleted should be(true)

    // attribute missing -> active
    event("223069722").status should be (None)
    event("223069722").isDeleted should be(false)

    // attribute unknown string -> active
    event("223069723").status should be(Some("foobar"))
    event("223069723").isDeleted should be(false)
  }
}
