package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.concurrent.Await
import org.joda.time.{DateTime, DateMidnight}
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class PlayerHead2HeadTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load the player head to head info" in {
    val (player1h2h, player2h2h) = Await.result(
      StubClient.playerHead2Head("250968", "355256", new DateMidnight(2013, 11, 3), new DateMidnight(2014, 2, 4)),
      1.second
    )

    player1h2h should have (
      'id ("250968"),
      'name ("Fernando Torres"),
      'totalGoals (3),
      'totalBookings (0),
      'totalDismissals (0),
      'totalSubstitutions (9)
    )
    player1h2h.goals.homeCount should equal(1)
    player1h2h.goals.awayCount should equal(2)
    player1h2h.goals.homeMatches.map(_.id) should equal(List("3631659"))
    player1h2h.goals.awayMatches.map(_.id) should equal(List("3632011", "3631915"))
    player1h2h.substitutions.homeMatches.map(_.id) should equal(List("3684147", "3679045", "3631659", "3643949"))
    player1h2h.substitutions.awayMatches.map(_.id) should equal(List("3690792", "3679046", "3631620", "3643939", "3667022"))

    player2h2h should have (
      'id ("355256"),
      'name ("Demba Ba"),
      'totalGoals (3),
      'totalBookings (0),
      'totalDismissals (0),
      'totalSubstitutions (11)
    )
    player2h2h.substitutions.homeCount should equal(6)
    player2h2h.substitutions.awayCount should equal(5)
  }

  "PaClient" should "load the player head to head information with competition filter" in {
    val (team1, team2) = Await.result(
      StubClient.playerHead2Head("300448", "494151", new DateMidnight(2013, 11, 3), new DateMidnight(2014, 2, 4), "100"),
      1.second
    )

    team1 should have (
      'id ("300448"),
      'name ("Abou Diaby"),
      'totalGoals (0),
      'totalBookings (0),
      'totalDismissals (0),
      'totalSubstitutions (0)
    )
    team2 should have (
      'id ("494151"),
      'name ("Adnan Januzaj"),
      'totalGoals (1),
      'totalBookings (3),
      'totalDismissals (0),
      'totalSubstitutions (7)
    )
  }
}
