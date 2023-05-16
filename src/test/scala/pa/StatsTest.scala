package pa


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StatsTest extends AnyFlatSpec with Matchers {

  "PaClient" should "load match stats" in {

    val stats = Await.result(StubClient.matchStats("3921571"), 10.seconds)

    val finalStats = stats.filter(_.interval == 90).head

    finalStats.homeTeam.corners should be(7)

    finalStats.homeTeam.shotsOnTarget should be(5)

    finalStats.awayPossession should be (34)

    val earlierStats = stats.filter(_.interval == 30).head

    earlierStats.homeTeam.corners should be(3)

    earlierStats.homeTeam.shotsOnTarget should be(0)

    earlierStats.awayPossession should be (33)

  }

  it should "load empty match stats" in {
    try {
      Await.result(StubClient.matchStats("3283333"), 10.seconds) should be (Nil)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }
  }
}
