package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class EventsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load a match" in {

    val theMatch = Await.result(StubClient.matchEvents("3507403"), 1.second).get

    theMatch.homeTeam should be (Team("9", "Liverpool"))
    theMatch.awayTeam should be (Team("4", "Chelsea"))

    theMatch.homeTeamScore should be (3)

    theMatch.awayTeamScore should be (1)

    theMatch.homeTeamGoals(0).players(0).name should be ("Jordan Henderson")

    theMatch.isResult should be (true)
  }

  it should "parse those dodgy matches with no events" in {
    Await.result(StubClient.matchEvents("3304257"), 1.second) should be (None)
  }

  it should "parse added time if the field is available" in {
    val theEvents = Await.result(StubClient.matchEvents("3704203"), 1.second).get

    val Some(event) = theEvents.events.find(_.id == Some("18882801"))

    event.matchTime should be (Some("(90 +5:33)"))
    event.eventTime should be (Some("90"))
    event.addedTime should be (Some("5:33"))
  }
}
