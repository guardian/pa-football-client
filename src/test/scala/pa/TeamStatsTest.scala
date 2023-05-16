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

     teamStats.offence.assists should have (Symbol("home") (19), Symbol("away")(25), Symbol("total") (44), Symbol("statDescription") ("Assists"), Symbol("statTypeId") ("234"))
     teamStats.offence.corners should have (Symbol("home") (123), Symbol("away")(107), Symbol("total") (230), Symbol("statDescription") ("Corners"), Symbol("statTypeId") ("190"))
     teamStats.offence.crosses should have (Symbol("home") (321), Symbol("away")(218), Symbol("total") (539), Symbol("statDescription") ("Crosses"), Symbol("statTypeId") ("148"))
     teamStats.offence.freeKicks should have (Symbol("home") (267), Symbol("away")(216), Symbol("total") (483), Symbol("statDescription") ("Free Kicks"), Symbol("statTypeId") ("159"))
     teamStats.offence.goals should have (Symbol("home") (29), Symbol("away")(28), Symbol("total") (57), Symbol("statDescription") ("Goals"), Symbol("statTypeId") ("78"))
     teamStats.offence.penalties should have (Symbol("home") (5), Symbol("away")(1), Symbol("total") (6), Symbol("statDescription") ("Penalties"), Symbol("statTypeId") ("46"))
     teamStats.offence.shotsOffTarget should have (Symbol("home") (193), Symbol("away")(173), Symbol("total") (366), Symbol("statDescription") ("Shots Off Target"), Symbol("statTypeId") ("164"))
     teamStats.offence.shotsOnTarget should have (Symbol("home") (103), Symbol("away")(93), Symbol("total") (196), Symbol("statDescription") ("Shots On Target"), Symbol("statTypeId") ("214"))
     teamStats.offence.throwIns should have (Symbol("home") (475), Symbol("away")(373), Symbol("total") (848), Symbol("statDescription") ("Throw Ins"), Symbol("statTypeId") ("144"))

     teamStats.offence.shotsOnTargetPercentage.home should equal(35)
     teamStats.offence.shotsOnTargetPercentage.away should equal(35)
     teamStats.offence.shotsOnTargetPercentage.total should equal(35)

     teamStats.defence.backPasses should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Back Passes"), Symbol("statTypeId") ("264"))
     teamStats.defence.blocks should have (Symbol("home") (29), Symbol("away")(32), Symbol("total") (61), Symbol("statDescription") ("Blocks"), Symbol("statTypeId") ("212"))
     teamStats.defence.clearances should have (Symbol("home") (384), Symbol("away")(395), Symbol("total") (779), Symbol("statDescription") ("Clearances"), Symbol("statTypeId") ("181"))
     teamStats.defence.goalKicks should have (Symbol("home") (127), Symbol("away")(153), Symbol("total") (280), Symbol("statDescription") ("Goal Kicks"), Symbol("statTypeId") ("153"))
     teamStats.defence.goalsAgainst should have (Symbol("home") (23), Symbol("away")(15), Symbol("total") (38), Symbol("statDescription") ("Goals Against"), Symbol("statTypeId") ("597"))
     teamStats.defence.ownGoals should have (Symbol("home") (1), Symbol("away")(1), Symbol("total") (2), Symbol("statDescription") ("Own Goals"), Symbol("statTypeId") ("102"))
     teamStats.defence.ownGoalsFor should have (Symbol("home") (0), Symbol("away")(4), Symbol("total") (4), Symbol("statDescription") ("Own Goals For"), Symbol("statTypeId") ("596"))
     teamStats.defence.saves should have (Symbol("home") (52), Symbol("away")(57), Symbol("total") (109), Symbol("statDescription") ("Saves"), Symbol("statTypeId") ("151"))

     teamStats.discipline.bookings should have (Symbol("home") (29), Symbol("away")(25), Symbol("total") (54), Symbol("statDescription") ("Bookings"), Symbol("statTypeId") ("37"))
     teamStats.discipline.dismissals should have (Symbol("home") (2), Symbol("away")(0), Symbol("total") (2), Symbol("statDescription") ("Dismissals"), Symbol("statTypeId") ("29"))
     teamStats.discipline.foulsAgainst should have (Symbol("home") (203), Symbol("away")(149), Symbol("total") (352), Symbol("statDescription") ("Fouls Against"), Symbol("statTypeId") ("173"))
     teamStats.discipline.foulsCommitted should have (Symbol("home") (169), Symbol("away")(178), Symbol("total") (347), Symbol("statDescription") ("Fouls Committed"), Symbol("statTypeId") ("170"))
     teamStats.discipline.handBalls should have (Symbol("home") (9), Symbol("away")(9), Symbol("total") (18), Symbol("statDescription") ("Hand Balls"), Symbol("statTypeId") ("255"))
     teamStats.discipline.offsides should have (Symbol("home") (37), Symbol("away")(29), Symbol("total") (66), Symbol("statDescription") ("Offsides"), Symbol("statTypeId") ("156"))
     teamStats.discipline.tenYards should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Ten Yards"), Symbol("statTypeId") ("273"))

     teamStats.substitutionsOff should have (Symbol("home") (53), Symbol("away")(49), Symbol("total") (102), Symbol("statDescription") ("Substitutions Off"), Symbol("statTypeId") ("72"))
     teamStats.substitutionsOn should have (Symbol("home") (53), Symbol("away")(49), Symbol("total") (102), Symbol("statDescription") ("Substitutions On"), Symbol("statTypeId") ("70"))
     teamStats.totalGoalsAgainst should have (Symbol("home") (24), Symbol("away")(16), Symbol("total") (40), Symbol("statDescription") ("Total Goals Against"), Symbol("statTypeId") ("599"))
     teamStats.totalGoalsFor should have (Symbol("home") (29), Symbol("away")(32), Symbol("total") (61), Symbol("statDescription") ("Total Goals For"), Symbol("statTypeId") ("598"))
   }

   "PaClient" should "load team stats for the given league" in {
     val teamStats = Await.result(
       StubClient.teamStats("19", LocalDate.of(2013, 8, 1), LocalDate.of(2014, 2, 5), "100"),
       10.seconds
     )

     teamStats.offence.assists should have (Symbol("home") (7), Symbol("away")(10), Symbol("total") (17), Symbol("statDescription") ("Assists"), Symbol("statTypeId") ("234"))
     teamStats.defence.backPasses should have (Symbol("home") (0), Symbol("away")(0), Symbol("total") (0), Symbol("statDescription") ("Back Passes"), Symbol("statTypeId") ("264"))
     teamStats.discipline.bookings should have (Symbol("home") (22), Symbol("away")(19), Symbol("total") (41), Symbol("statDescription") ("Bookings"), Symbol("statTypeId") ("37"))
     teamStats.substitutionsOff should have (Symbol("home") (35), Symbol("away")(33), Symbol("total") (68), Symbol("statDescription") ("Substitutions Off"), Symbol("statTypeId") ("72"))
   }
 }
