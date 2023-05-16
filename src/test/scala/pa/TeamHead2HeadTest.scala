package pa

import java.time.LocalDate


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class TeamHead2HeadTest extends AnyFlatSpec with Matchers {

  "PaClient" should "load the head to head information" in {
    val (team1, team2) = Await.result(
      StubClient.teamHead2Head("4", "12", LocalDate.of(2013, 12, 2), LocalDate.of(2014, 1, 24)),
      10.seconds
    )

    team1 should have (
      Symbol("id") ("4"),
      Symbol("name") ("Chelsea"),
      Symbol("totalGoals") (23),
      Symbol("totalBookings") (20),
      Symbol("totalDismissals") (0),
      Symbol("totalSubstitutions") (36)
    )
    team2 should have (
      Symbol("id") ("12"),
      Symbol("name") ("Man Utd"),
      Symbol("totalGoals") (21),
      Symbol("totalBookings") (24),
      Symbol("totalDismissals") (3),
      Symbol("totalSubstitutions") (38)
    )

    team1.goals.homeCount should equal(9)
    team1.goals.awayCount should equal(14)
    team1.goals.homeMatches.map(_.id) should equal(List("3684147", "3679045", "3631778", "3631659", "3667042"))
    team1.goals.awayMatches.map(_.id) should equal(List("3632011", "3690792", "3631915", "3684554", "3631620", "3643939"))

    team2.dismissals.homeCount should equal(1)
    team2.dismissals.awayCount should equal(2)
    team2.dismissals.homeMatches.map(_.id) should equal(List("3690793"))
    team2.dismissals.awayMatches.map(_.id) should equal(List("3684147", "3631780"))
  }
}
