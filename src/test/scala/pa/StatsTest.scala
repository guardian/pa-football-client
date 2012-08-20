package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class StatsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load match stats" in {

    val stats = StubClient.matchStats("3409881")

    stats.homeTeam.corners should be(9)

    stats.awayTeam.shotsOnTarget should be(7)
  }
}
