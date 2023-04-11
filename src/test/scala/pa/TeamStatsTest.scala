package pa

import java.time.LocalDate


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class TeamStatsTest extends AnyFlatSpec with Matchers {
   "PaClient" should "load team stats" in {
     val teamStats = Await.result(
       StubClient.teamStats("19", LocalDate.of(2013, 8, 1), LocalDate.of(2014, 2, 5)),
       10.seconds
     )

     teamStats.offence.assists should have ('home (19), 'away(25), 'total (44), 'statDescription ("Assists"), 'statTypeId ("234"))
     teamStats.offence.corners should have ('home (123), 'away(107), 'total (230), 'statDescription ("Corners"), 'statTypeId ("190"))
     teamStats.offence.crosses should have ('home (321), 'away(218), 'total (539), 'statDescription ("Crosses"), 'statTypeId ("148"))
     teamStats.offence.freeKicks should have ('home (267), 'away(216), 'total (483), 'statDescription ("Free Kicks"), 'statTypeId ("159"))
     teamStats.offence.goals should have ('home (29), 'away(28), 'total (57), 'statDescription ("Goals"), 'statTypeId ("78"))
     teamStats.offence.penalties should have ('home (5), 'away(1), 'total (6), 'statDescription ("Penalties"), 'statTypeId ("46"))
     teamStats.offence.shotsOffTarget should have ('home (193), 'away(173), 'total (366), 'statDescription ("Shots Off Target"), 'statTypeId ("164"))
     teamStats.offence.shotsOnTarget should have ('home (103), 'away(93), 'total (196), 'statDescription ("Shots On Target"), 'statTypeId ("214"))
     teamStats.offence.throwIns should have ('home (475), 'away(373), 'total (848), 'statDescription ("Throw Ins"), 'statTypeId ("144"))

     teamStats.offence.shotsOnTargetPercentage.home should equal(35)
     teamStats.offence.shotsOnTargetPercentage.away should equal(35)
     teamStats.offence.shotsOnTargetPercentage.total should equal(35)

     teamStats.defence.backPasses should have ('home (0), 'away(0), 'total (0), 'statDescription ("Back Passes"), 'statTypeId ("264"))
     teamStats.defence.blocks should have ('home (29), 'away(32), 'total (61), 'statDescription ("Blocks"), 'statTypeId ("212"))
     teamStats.defence.clearances should have ('home (384), 'away(395), 'total (779), 'statDescription ("Clearances"), 'statTypeId ("181"))
     teamStats.defence.goalKicks should have ('home (127), 'away(153), 'total (280), 'statDescription ("Goal Kicks"), 'statTypeId ("153"))
     teamStats.defence.goalsAgainst should have ('home (23), 'away(15), 'total (38), 'statDescription ("Goals Against"), 'statTypeId ("597"))
     teamStats.defence.ownGoals should have ('home (1), 'away(1), 'total (2), 'statDescription ("Own Goals"), 'statTypeId ("102"))
     teamStats.defence.ownGoalsFor should have ('home (0), 'away(4), 'total (4), 'statDescription ("Own Goals For"), 'statTypeId ("596"))
     teamStats.defence.saves should have ('home (52), 'away(57), 'total (109), 'statDescription ("Saves"), 'statTypeId ("151"))

     teamStats.discipline.bookings should have ('home (29), 'away(25), 'total (54), 'statDescription ("Bookings"), 'statTypeId ("37"))
     teamStats.discipline.dismissals should have ('home (2), 'away(0), 'total (2), 'statDescription ("Dismissals"), 'statTypeId ("29"))
     teamStats.discipline.foulsAgainst should have ('home (203), 'away(149), 'total (352), 'statDescription ("Fouls Against"), 'statTypeId ("173"))
     teamStats.discipline.foulsCommitted should have ('home (169), 'away(178), 'total (347), 'statDescription ("Fouls Committed"), 'statTypeId ("170"))
     teamStats.discipline.handBalls should have ('home (9), 'away(9), 'total (18), 'statDescription ("Hand Balls"), 'statTypeId ("255"))
     teamStats.discipline.offsides should have ('home (37), 'away(29), 'total (66), 'statDescription ("Offsides"), 'statTypeId ("156"))
     teamStats.discipline.tenYards should have ('home (0), 'away(0), 'total (0), 'statDescription ("Ten Yards"), 'statTypeId ("273"))

     teamStats.substitutionsOff should have ('home (53), 'away(49), 'total (102), 'statDescription ("Substitutions Off"), 'statTypeId ("72"))
     teamStats.substitutionsOn should have ('home (53), 'away(49), 'total (102), 'statDescription ("Substitutions On"), 'statTypeId ("70"))
     teamStats.totalGoalsAgainst should have ('home (24), 'away(16), 'total (40), 'statDescription ("Total Goals Against"), 'statTypeId ("599"))
     teamStats.totalGoalsFor should have ('home (29), 'away(32), 'total (61), 'statDescription ("Total Goals For"), 'statTypeId ("598"))
   }

   "PaClient" should "load team stats for the given league" in {
     val teamStats = Await.result(
       StubClient.teamStats("19", LocalDate.of(2013, 8, 1), LocalDate.of(2014, 2, 5), "100"),
       10.seconds
     )

     teamStats.offence.assists should have ('home (7), 'away(10), 'total (17), 'statDescription ("Assists"), 'statTypeId ("234"))
     teamStats.defence.backPasses should have ('home (0), 'away(0), 'total (0), 'statDescription ("Back Passes"), 'statTypeId ("264"))
     teamStats.discipline.bookings should have ('home (22), 'away(19), 'total (41), 'statDescription ("Bookings"), 'statTypeId ("37"))
     teamStats.substitutionsOff should have ('home (35), 'away(33), 'total (68), 'statDescription ("Substitutions Off"), 'statTypeId ("72"))
   }
 }
