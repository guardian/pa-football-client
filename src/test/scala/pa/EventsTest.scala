package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class EventsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load a match" in {

    val theMatch = StubClient.matchEvents("3507403")

    theMatch.homeTeam should be (Team("9", "Liverpool"))
    theMatch.awayTeam should be (Team("4", "Chelsea"))

    println(theMatch.goals)


    theMatch.homeTeamScore should be (3)

    theMatch.awayTeamScore should be (1)

    theMatch.homeTeamGoals(0).players(0).name should be ("Jordan Henderson")
  }
}
