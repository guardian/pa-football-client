package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class LineUpTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load lineups for a match" in {

    val lineUp = Await.result(StubClient.lineUp("3560717"), 1.second)

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
    david.rating should be (Some(5))
    david.timeOnPitch should be ("120:00")

    david.events(2) should be (LineUpEvent("throw in", "29", "(29)"))

    homeTeam.players.find(_.name == "Henrique Hilario").get.rating should be (None)
  }
}
