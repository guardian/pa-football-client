package pa

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class LineUpTest extends FlatSpec with Matchers {

  "PaClient" should "load lineups for a match" in {

    val lineUp = Await.result(StubClient.lineUp("3560717"), 10.seconds)

    lineUp.homeTeamPossession should be (52)
    lineUp.awayTeamPossession should be (48)

    val homeTeam = lineUp.homeTeam

    homeTeam.name should be ("Chelsea")
    homeTeam.id should be ("4")
    homeTeam.teamColour should be ("#005CA4")
    homeTeam.manager.id should be ("28246")
    homeTeam.manager.name should be ("Roberto Di Matteo")
    homeTeam.formation should be ("4-2-3-1")
    homeTeam.shotsOn should be (14)
    homeTeam.shotsOff should be (15)
    homeTeam.fouls should be (17)
    homeTeam.corners should be (6)
    homeTeam.offsides should be (2)
    homeTeam.bookings should be (5)
    homeTeam.dismissals should be (0)

    lineUp.awayTeam.name should be ("Man Utd")


    val david = homeTeam.players(3)
    david.name should be ("David Luiz")
    david.firstName should be ("David")
    david.lastName should be ("Luiz")
    david.shirtNumber should be ("4")
    david.position should be ("Defender")
    david.substitute should be (false)
    david.rating should be (None)
    david.timeOnPitch should be ("130:45")

    david.events(2) should be (LineUpEvent("throw in", "29", "(29)"))

    homeTeam.players.find(_.name == "Henrique Hilario").get.rating should be (None)
  }
}
