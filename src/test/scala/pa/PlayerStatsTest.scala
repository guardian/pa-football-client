package pa

import java.time.LocalDate


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class PlayerStatsTest extends AnyFlatSpec with Matchers {
  "PaClient" should "load player stats" in {
    val playerStats = Await.result(
      StubClient.playerStats("237670", LocalDate.of(2013, 8, 1), LocalDate.of(2014, 2, 5)),
      10.seconds
    )

    playerStats.offence.assists should have (Symbol("home") (1), Symbol("away")(0), Symbol("total") (1), Symbol("statDescription") ("Assists"), Symbol("statTypeId") ("234"))
    playerStats.offence.corners should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Corners"), Symbol("statTypeId") ("190"))
    playerStats.offence.crosses should have (Symbol("home") (7), Symbol("away")(3), Symbol("total") (10), Symbol("statDescription") ("Crosses"), Symbol("statTypeId") ("148"))
    playerStats.offence.freeKicks should have (Symbol("home") (1), Symbol("away")(0), Symbol("total") (1), Symbol("statDescription") ("Free Kicks"), Symbol("statTypeId") ("159"))
    playerStats.offence.goals should have (Symbol("home") (1), Symbol("away")(5), Symbol("total") (6), Symbol("statDescription") ("Goals"), Symbol("statTypeId") ("78"))
    playerStats.offence.penalties should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Penalties"), Symbol("statTypeId") ("46"))
    playerStats.offence.shotsOffTarget should have (Symbol("home") (7), Symbol("away")(5), Symbol("total") (12), Symbol("statDescription") ("Shots Off Target"), Symbol("statTypeId") ("164"))
    playerStats.offence.shotsOnTarget should have (Symbol("home") (1), Symbol("away")(10), Symbol("total") (11), Symbol("statDescription") ("Shots On Target"), Symbol("statTypeId") ("214"))
    playerStats.offence.throwIns should have (Symbol("home") (2), Symbol("away")(1), Symbol("total") (3), Symbol("statDescription") ("Throw Ins"), Symbol("statTypeId") ("144"))

    playerStats.offence.shotsOnTargetPercentage.home should equal(13)
    playerStats.offence.shotsOnTargetPercentage.away should equal(67)
    playerStats.offence.shotsOnTargetPercentage.total should equal(48)

    playerStats.defence.backPasses should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Back Passes"), Symbol("statTypeId") ("264"))
    playerStats.defence.blocks should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Blocks"), Symbol("statTypeId") ("212"))
    playerStats.defence.clearances should have (Symbol("home") (2), Symbol("away")(7), Symbol("total") (9), Symbol("statDescription") ("Clearances"), Symbol("statTypeId") ("181"))
    playerStats.defence.goalKicks should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Goal Kicks"), Symbol("statTypeId") ("153"))
    playerStats.defence.goalsAgainst should have (Symbol("home") (5), Symbol("away")(1), Symbol("total") (6), Symbol("statDescription") ("Goals Against"), Symbol("statTypeId") ("597"))
    playerStats.defence.ownGoals should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Own Goals"), Symbol("statTypeId") ("102"))
    playerStats.defence.ownGoalsFor should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Own Goals For"), Symbol("statTypeId") ("596"))
    playerStats.defence.saves should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Saves"), Symbol("statTypeId") ("151"))

    playerStats.discipline.bookings should have (Symbol("home") (1), Symbol("away")(0), Symbol("total") (1), Symbol("statDescription") ("Bookings"), Symbol("statTypeId") ("37"))
    playerStats.discipline.dismissals should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Dismissals"), Symbol("statTypeId") ("29"))
    playerStats.discipline.foulsAgainst should have (Symbol("home") (3), Symbol("away")(3), Symbol("total") (6), Symbol("statDescription") ("Fouls Against"), Symbol("statTypeId") ("173"))
    playerStats.discipline.foulsCommitted should have (Symbol("home") (7), Symbol("away")(7), Symbol("total") (14), Symbol("statDescription") ("Fouls Committed"), Symbol("statTypeId") ("170"))
    playerStats.discipline.handBalls should have (Symbol("home") (1), Symbol("away")(0), Symbol("total") (1), Symbol("statDescription") ("Hand Balls"), Symbol("statTypeId") ("255"))
    playerStats.discipline.offsides should have (Symbol("home") (5), Symbol("away")(10), Symbol("total") (15), Symbol("statDescription") ("Offsides"), Symbol("statTypeId") ("156"))
    playerStats.discipline.tenYards should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Ten Yards"), Symbol("statTypeId") ("273"))

    playerStats.substitutionsOff should have (Symbol("home") (2), Symbol("away")(2), Symbol("total") (4), Symbol("statDescription") ("Substitutions Off"), Symbol("statTypeId") ("72"))
    playerStats.substitutionsOn should have (Symbol("home") (0), Symbol("away")(1), Symbol("total") (1), Symbol("statDescription") ("Substitutions On"), Symbol("statTypeId") ("70"))
    playerStats.totalGoalsAgainst should have (Symbol("home") (5), Symbol("away")(1), Symbol("total") (6), Symbol("statDescription") ("Total Goals Against"), Symbol("statTypeId") ("599"))
    playerStats.totalGoalsFor should have (Symbol("home") (1), Symbol("away")(5), Symbol("total") (6), Symbol("statDescription") ("Total Goals For"), Symbol("statTypeId") ("598"))
  }

  "PaClient" should "load player stats for the given team" in {
    val playerStats = Await.result(
      StubClient.playerStats("237670", LocalDate.of(2013, 8, 1), LocalDate.of(2014, 2, 5), "19"),
      10.seconds
    )

    playerStats.offence.crosses should have (Symbol("home") (7), Symbol("away")(3), Symbol("total") (10), Symbol("statDescription") ("Crosses"), Symbol("statTypeId") ("148"))
    playerStats.defence.clearances should have (Symbol("home") (2), Symbol("away")(7), Symbol("total") (9), Symbol("statDescription") ("Clearances"), Symbol("statTypeId") ("181"))
    playerStats.discipline.bookings should have (Symbol("home") (1), Symbol("away")(0), Symbol("total") (1), Symbol("statDescription") ("Bookings"), Symbol("statTypeId") ("37"))
    playerStats.substitutionsOff should have (Symbol("home") (2), Symbol("away")(2), Symbol("total") (4), Symbol("statDescription") ("Substitutions Off"), Symbol("statTypeId") ("72"))
  }

  "PaClient" should "load player stats for the given team and competition" in {
    val playerStats = Await.result(
      StubClient.playerStats("237670", LocalDate.of(2013, 8, 1),  LocalDate.of(2014, 2, 5), "19", "100"),
      10.seconds
    )

    playerStats.offence.crosses should have (Symbol("home") (3), Symbol("away")(3), Symbol("total") (6), Symbol("statDescription") ("Crosses"), Symbol("statTypeId") ("148"))
    playerStats.defence.backPasses should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Back Passes"), Symbol("statTypeId") ("264"))
    playerStats.discipline.bookings should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Bookings"), Symbol("statTypeId") ("37"))
    playerStats.substitutionsOff should have (Symbol("home") (1), Symbol("away")(2), Symbol("total") (3), Symbol("statDescription") ("Substitutions Off"), Symbol("statTypeId") ("72"))
  }
}
