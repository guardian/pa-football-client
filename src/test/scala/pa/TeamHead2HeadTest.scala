package pa

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import scala.concurrent.Await
import org.joda.time.LocalDate
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class TeamHead2HeadTest extends FlatSpec with Matchers {

  "PaClient" should "load the head to head information" in {
    val (team1, team2) = Await.result(
      StubClient.teamHead2Head("4", "12", new LocalDate(2013, 12, 2), new LocalDate(2014, 1, 24)),
      10.seconds
    )

    team1 should have (
      'id ("4"),
      'name ("Chelsea"),
      'totalGoals (23),
      'totalBookings (20),
      'totalDismissals (0),
      'totalSubstitutions (36)
    )
    team2 should have (
      'id ("12"),
      'name ("Man Utd"),
      'totalGoals (21),
      'totalBookings (24),
      'totalDismissals (3),
      'totalSubstitutions (38)
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
