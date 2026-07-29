package pa

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class LineUpEnhancedTest extends AnyFlatSpec with Matchers {

  "PaClient" should "load lineups for a match" in {

    val lineUp = Await.result(StubClient.lineUpEnhanced("4566691"), 10.seconds)

    lineUp.homeTeamPossession should be (67)
    lineUp.awayTeamPossession should be (33)

    val homeTeam = lineUp.homeTeam

    homeTeam.name should be ("Spain")
    homeTeam.id should be ("999")
    homeTeam.teamColour should be ("#ED1322")
    homeTeam.manager.id should be ("679163")
    homeTeam.manager.name should be ("Luis De La Fuente")
    homeTeam.formation should be ("4-2-3-1")
    homeTeam.shotsOn should be (12)
    homeTeam.shotsOff should be (8)
    homeTeam.fouls should be (21)
    homeTeam.corners should be (9)
    homeTeam.offsides should be (4)
    homeTeam.bookings should be (0)
    homeTeam.dismissals should be (0)

    lineUp.awayTeam.name should be ("Argentina")


    val laporte = homeTeam.players(3)
    laporte.name should be ("Aymeric Laporte")
    laporte.firstName should be ("Aymeric")
    laporte.lastName should be ("Laporte")
    laporte.shirtNumber should be ("14")
    laporte.position should be ("Defender")
    laporte.substitute should be (false)
    laporte.rating should be (None)
    laporte.timeOnPitch should be ("110:46")

    laporte.events.last should be (
      LineUpEventEnhanced("39501462", "substitution", "98:24", "0:00")
    )

    homeTeam.players.find(_.name == "Marc Cucurella").get.rating should be (None)
  }
}
