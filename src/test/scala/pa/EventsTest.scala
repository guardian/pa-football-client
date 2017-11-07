package pa

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class EventsTest extends FlatSpec with Matchers {
  it should "load a match and match events" in {
    val theMatch = Await.result(StubClient.matchEvents("3888465"), 10.seconds).get

    theMatch.homeTeam should be (Team("999", "Spain"))
    theMatch.awayTeam should be (Team("6318", "Czech Republic"))

    theMatch.homeTeamScore should be (1)

    theMatch.awayTeamScore should be (0)

    theMatch.homeTeamGoals(0).players(0).name should be ("Gerard Pique")

    theMatch.isResult should be (true)

    val Some(event) = theMatch.events.find(_.id == Some("22306998"))

    event should have(
      'matchTime (Some("(90 +2:05)")),
      'eventTime (Some("90")),
      'addedTime (Some("2:05"))
    )
  }
}
