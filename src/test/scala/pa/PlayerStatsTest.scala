package pa

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class PlayerStatsTest extends FlatSpec with Matchers {
  "PaClient" should "load player stats" in {
    val playerStats = Await.result(
      StubClient.playerStats("237670", LocalDate.of(2013, 8, 1), LocalDate.of(2014, 2, 5)),
      10.seconds
    )

    playerStats.offence.assists should have ('home (1), 'away(0), 'total (1), 'statDescription ("Assists"), 'statTypeId ("234"))
    playerStats.offence.corners should have ('home (0), 'away(0), 'total (0), 'statDescription ("Corners"), 'statTypeId ("190"))
    playerStats.offence.crosses should have ('home (7), 'away(3), 'total (10), 'statDescription ("Crosses"), 'statTypeId ("148"))
    playerStats.offence.freeKicks should have ('home (1), 'away(0), 'total (1), 'statDescription ("Free Kicks"), 'statTypeId ("159"))
    playerStats.offence.goals should have ('home (1), 'away(5), 'total (6), 'statDescription ("Goals"), 'statTypeId ("78"))
    playerStats.offence.penalties should have ('home (0), 'away(0), 'total (0), 'statDescription ("Penalties"), 'statTypeId ("46"))
    playerStats.offence.shotsOffTarget should have ('home (7), 'away(5), 'total (12), 'statDescription ("Shots Off Target"), 'statTypeId ("164"))
    playerStats.offence.shotsOnTarget should have ('home (1), 'away(10), 'total (11), 'statDescription ("Shots On Target"), 'statTypeId ("214"))
    playerStats.offence.throwIns should have ('home (2), 'away(1), 'total (3), 'statDescription ("Throw Ins"), 'statTypeId ("144"))

    playerStats.offence.shotsOnTargetPercentage.home should equal(13)
    playerStats.offence.shotsOnTargetPercentage.away should equal(67)
    playerStats.offence.shotsOnTargetPercentage.total should equal(48)

    playerStats.defence.backPasses should have ('home (0), 'away(0), 'total (0), 'statDescription ("Back Passes"), 'statTypeId ("264"))
    playerStats.defence.blocks should have ('home (0), 'away(0), 'total (0), 'statDescription ("Blocks"), 'statTypeId ("212"))
    playerStats.defence.clearances should have ('home (2), 'away(7), 'total (9), 'statDescription ("Clearances"), 'statTypeId ("181"))
    playerStats.defence.goalKicks should have ('home (0), 'away(0), 'total (0), 'statDescription ("Goal Kicks"), 'statTypeId ("153"))
    playerStats.defence.goalsAgainst should have ('home (5), 'away(1), 'total (6), 'statDescription ("Goals Against"), 'statTypeId ("597"))
    playerStats.defence.ownGoals should have ('home (0), 'away(0), 'total (0), 'statDescription ("Own Goals"), 'statTypeId ("102"))
    playerStats.defence.ownGoalsFor should have ('home (0), 'away(0), 'total (0), 'statDescription ("Own Goals For"), 'statTypeId ("596"))
    playerStats.defence.saves should have ('home (0), 'away(0), 'total (0), 'statDescription ("Saves"), 'statTypeId ("151"))

    playerStats.discipline.bookings should have ('home (1), 'away(0), 'total (1), 'statDescription ("Bookings"), 'statTypeId ("37"))
    playerStats.discipline.dismissals should have ('home (0), 'away(0), 'total (0), 'statDescription ("Dismissals"), 'statTypeId ("29"))
    playerStats.discipline.foulsAgainst should have ('home (3), 'away(3), 'total (6), 'statDescription ("Fouls Against"), 'statTypeId ("173"))
    playerStats.discipline.foulsCommitted should have ('home (7), 'away(7), 'total (14), 'statDescription ("Fouls Committed"), 'statTypeId ("170"))
    playerStats.discipline.handBalls should have ('home (1), 'away(0), 'total (1), 'statDescription ("Hand Balls"), 'statTypeId ("255"))
    playerStats.discipline.offsides should have ('home (5), 'away(10), 'total (15), 'statDescription ("Offsides"), 'statTypeId ("156"))
    playerStats.discipline.tenYards should have ('home (0), 'away(0), 'total (0), 'statDescription ("Ten Yards"), 'statTypeId ("273"))

    playerStats.substitutionsOff should have ('home (2), 'away(2), 'total (4), 'statDescription ("Substitutions Off"), 'statTypeId ("72"))
    playerStats.substitutionsOn should have ('home (0), 'away(1), 'total (1), 'statDescription ("Substitutions On"), 'statTypeId ("70"))
    playerStats.totalGoalsAgainst should have ('home (5), 'away(1), 'total (6), 'statDescription ("Total Goals Against"), 'statTypeId ("599"))
    playerStats.totalGoalsFor should have ('home (1), 'away(5), 'total (6), 'statDescription ("Total Goals For"), 'statTypeId ("598"))
  }

  "PaClient" should "load player stats for the given team" in {
    val playerStats = Await.result(
      StubClient.playerStats("237670", LocalDate.of(2013, 8, 1), LocalDate.of(2014, 2, 5), "19"),
      10.seconds
    )

    playerStats.offence.crosses should have ('home (7), 'away(3), 'total (10), 'statDescription ("Crosses"), 'statTypeId ("148"))
    playerStats.defence.clearances should have ('home (2), 'away(7), 'total (9), 'statDescription ("Clearances"), 'statTypeId ("181"))
    playerStats.discipline.bookings should have ('home (1), 'away(0), 'total (1), 'statDescription ("Bookings"), 'statTypeId ("37"))
    playerStats.substitutionsOff should have ('home (2), 'away(2), 'total (4), 'statDescription ("Substitutions Off"), 'statTypeId ("72"))
  }

  "PaClient" should "load player stats for the given team and competition" in {
    val playerStats = Await.result(
      StubClient.playerStats("237670", LocalDate.of(2013, 8, 1),  LocalDate.of(2014, 2, 5), "19", "100"),
      10.seconds
    )

    playerStats.offence.crosses should have ('home (3), 'away(3), 'total (6), 'statDescription ("Crosses"), 'statTypeId ("148"))
    playerStats.defence.backPasses should have ('home (0), 'away(0), 'total (0), 'statDescription ("Back Passes"), 'statTypeId ("264"))
    playerStats.discipline.bookings should have ('home (0), 'away(0), 'total (0), 'statDescription ("Bookings"), 'statTypeId ("37"))
    playerStats.substitutionsOff should have ('home (1), 'away(2), 'total (3), 'statDescription ("Substitutions Off"), 'statTypeId ("72"))
  }
}
