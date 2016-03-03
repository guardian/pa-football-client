package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class StatsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load match stats" in {

    val stats = Await.result(StubClient.matchStats("3409881"), 1.second)

    val finalStats = stats.filter(_.interval == 90).head

    finalStats.homeTeam.corners should be(9)

    finalStats.awayTeam.shotsOnTarget should be(7)

    finalStats.awayPossession should be (51)

    val earlierStats = stats.filter(_.interval == 30).head

    earlierStats.homeTeam.corners should be(4)

    earlierStats.awayTeam.shotsOnTarget should be(2)

    earlierStats.awayPossession should be (49)

  }

  it should "load empty match stats" in {
    try {
      Await.result(StubClient.matchStats("3283333"), 1.second) should be (Nil)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }
  }
}
