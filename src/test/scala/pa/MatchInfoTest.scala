package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MatchInfoTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load matchInfo" in {
    val theMatch = Await.result(StubClient.matchInfo("3695171"), 1.second)

    theMatch.id should be("3695171")
    theMatch.round should be(Some(Round("7", "Semi-Final Second Leg")))
    theMatch.leg should be("2")
    theMatch.liveMatch should be(false)
    theMatch.result should be(true)
    theMatch.previewAvailable should be(true)
    theMatch.reportAvailable should be(true)
    theMatch.lineupsAvailable should be(true)
    theMatch.matchStatus should be("FT")
    theMatch.attendance should be(None)
    theMatch.referee should be(Some(Official("182780", "Lee Mason")))
    theMatch.venue should be(Some(Venue("114", "Old Trafford")))

    val homeTeam = theMatch.homeTeam

    homeTeam.id should be("12")
    homeTeam.name should be("Man Utd")
    homeTeam.score should be(Some(2))
    homeTeam.htScore should be(Some(1))
    homeTeam.aggregateScore should be(Some(3))
    homeTeam.scorers should be(Some("Jonny Evans (37),Javier Hernandez (120 +0:28)"))

    val awayTeam = theMatch.awayTeam

    awayTeam.id should be("39")
    awayTeam.name should be("Sunderland")
    awayTeam.score should be(Some(1))
    awayTeam.htScore should be(Some(0))
    awayTeam.aggregateScore should be(Some(3))
    awayTeam.scorers should be(Some("Phil Bardsley (119)"))

    theMatch.comments should be(Some("(Sunderland win 2-1 on penalties)"))
  }
}
