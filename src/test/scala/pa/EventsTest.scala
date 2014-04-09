package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class EventsTest extends FlatSpec with ShouldMatchers {
  /** The original XML format did not have the 'addedTime' field */
  "PaClient" should "load a match in the original XML format" in {
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

  it should "load a match in the new XML format" in {
    val theEvents = Await.result(StubClient.matchEvents("3704203"), 1.second).get

    theEvents should have(
      'homeTeam (Team("188", "Crawley Town")),
      'awayTeam (Team("1073", "Stevenage")),
      'homeTeamScore (1),
      'awayTeamScore (1),
      'isResult (true)
    )

    val Some(event) = theEvents.events.find(_.id == Some("18882801"))

    event should have(
      'matchTime (Some("(90 +5:33)")),
      'eventTime (Some("90")),
      'addedTime (Some("5:33"))
    )
  }
}
