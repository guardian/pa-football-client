package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.DateMidnight
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class TeamStatsTest extends FlatSpec with ShouldMatchers {
   "PaClient" should "load team stats" in {
     val teamStats = Await.result(
       StubClient.teamStats("19", new DateMidnight(2013, 8, 1), new DateMidnight(2014, 2, 5)),
       1.second
     )

     teamStats.offence.assists should have ('home (0), 'away(1), 'total (1), 'statDescription ("Assists"), 'statTypeId ("234"))
     teamStats.offence.corners should have ('home (5), 'away(6), 'total (11), 'statDescription ("Corners"), 'statTypeId ("190"))
     teamStats.offence.crosses should have ('home (6), 'away(7), 'total (13), 'statDescription ("Crosses"), 'statTypeId ("148"))
     teamStats.offence.freeKicks should have ('home (10), 'away(11), 'total (21), 'statDescription ("Free Kicks"), 'statTypeId ("159"))
     teamStats.offence.goals should have ('home (12), 'away(13), 'total (25), 'statDescription ("Goals"), 'statTypeId ("78"))
     teamStats.offence.penalties should have ('home (18), 'away(19), 'total (37), 'statDescription ("Penalties"), 'statTypeId ("46"))
     teamStats.offence.shotsOffTarget should have ('home (20), 'away(10), 'total (30), 'statDescription ("Shots Off Target"), 'statTypeId ("164"))
     teamStats.offence.shotsOnTarget should have ('home (21), 'away(22), 'total (43), 'statDescription ("Shots On Target"), 'statTypeId ("214"))
     teamStats.offence.throwIns should have ('home (25), 'away(26), 'total (51), 'statDescription ("Throw Ins"), 'statTypeId ("144"))

     teamStats.offence.shotsOnTargetPercentage.home should equal(51)
     teamStats.offence.shotsOnTargetPercentage.away should equal(69)
     teamStats.offence.shotsOnTargetPercentage.total should equal(59)

     teamStats.defence.backPasses should have ('home (1), 'away(2), 'total (3), 'statDescription ("Back Passes"), 'statTypeId ("264"))
     teamStats.defence.blocks should have ('home (2), 'away(3), 'total (5), 'statDescription ("Blocks"), 'statTypeId ("212"))
     teamStats.defence.clearances should have ('home (4), 'away(5), 'total (9), 'statDescription ("Clearances"), 'statTypeId ("181"))
     teamStats.defence.goalKicks should have ('home (11), 'away(12), 'total (23), 'statDescription ("Goal Kicks"), 'statTypeId ("153"))
     teamStats.defence.goalsAgainst should have ('home (13), 'away(14), 'total (27), 'statDescription ("Goals Against"), 'statTypeId ("597"))
     teamStats.defence.ownGoals should have ('home (16), 'away(17), 'total (33), 'statDescription ("Own Goals"), 'statTypeId ("102"))
     teamStats.defence.ownGoalsFor should have ('home (17), 'away(18), 'total (35), 'statDescription ("Own Goals For"), 'statTypeId ("596"))
     teamStats.defence.saves should have ('home (19), 'away(20), 'total (39), 'statDescription ("Saves"), 'statTypeId ("151"))

     teamStats.discipline.bookings should have ('home (3), 'away(4), 'total (7), 'statDescription ("Bookings"), 'statTypeId ("37"))
     teamStats.discipline.dismissals should have ('home (7), 'away(8), 'total (15), 'statDescription ("Dismissals"), 'statTypeId ("29"))
     teamStats.discipline.foulsAgainst should have ('home (8), 'away(9), 'total (17), 'statDescription ("Fouls Against"), 'statTypeId ("173"))
     teamStats.discipline.foulsCommitted should have ('home (9), 'away(10), 'total (19), 'statDescription ("Fouls Committed"), 'statTypeId ("170"))
     teamStats.discipline.handBalls should have ('home (14), 'away(15), 'total (29), 'statDescription ("Hand Balls"), 'statTypeId ("255"))
     teamStats.discipline.offsides should have ('home (15), 'away(16), 'total (31), 'statDescription ("Offsides"), 'statTypeId ("156"))
     teamStats.discipline.tenYards should have ('home (24), 'away(25), 'total (49), 'statDescription ("Ten Yards"), 'statTypeId ("273"))

     teamStats.substitutionsOff should have ('home (22), 'away(23), 'total (45), 'statDescription ("Substitutions Off"), 'statTypeId ("72"))
     teamStats.substitutionsOn should have ('home (23), 'away(24), 'total (47), 'statDescription ("Substitutions On"), 'statTypeId ("70"))
     teamStats.totalGoalsAgainst should have ('home (26), 'away(27), 'total (53), 'statDescription ("Total Goals Against"), 'statTypeId ("599"))
     teamStats.totalGoalsFor should have ('home (27), 'away(28), 'total (55), 'statDescription ("Total Goals For"), 'statTypeId ("598"))
   }

   "PaClient" should "load team stats for the given league" in {
     val teamStats = Await.result(
       StubClient.teamStats("19", new DateMidnight(2013, 8, 1), new DateMidnight(2014, 2, 5), "100"),
       1.second
     )

     teamStats.offence.assists should have ('home (7), 'away(10), 'total (17), 'statDescription ("Assists"), 'statTypeId ("234"))
     teamStats.defence.backPasses should have ('home (0), 'away(0), 'total (0), 'statDescription ("Back Passes"), 'statTypeId ("264"))
     teamStats.discipline.bookings should have ('home (22), 'away(19), 'total (41), 'statDescription ("Bookings"), 'statTypeId ("37"))
     teamStats.substitutionsOff should have ('home (35), 'away(33), 'total (68), 'statDescription ("Substitutions Off"), 'statTypeId ("72"))
   }
 }
