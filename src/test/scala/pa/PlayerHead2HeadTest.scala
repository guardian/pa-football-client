package pa

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class PlayerHead2HeadTest extends FlatSpec with Matchers {
  "PaClient" should "load the player head to head info" in {
    val (player1h2h, player2h2h) = Await.result(
      StubClient.playerHead2Head("250968", "355256", LocalDate.of(2013, 11, 3), LocalDate.of(2014, 2, 4)),
      10.seconds
    )

    player1h2h should have (
      Symbol("id") ("250968"),
      Symbol("name") ("Fernando Torres"),
      Symbol("totalGoals") (3),
      Symbol("totalBookings") (0),
      Symbol("totalDismissals") (0),
      Symbol("totalSubstitutions") (9)
    )
    player1h2h.goals.homeCount should equal(1)
    player1h2h.goals.awayCount should equal(2)
    player1h2h.goals.homeMatches.map(_.id) should equal(List("3631659"))
    player1h2h.goals.awayMatches.map(_.id) should equal(List("3632011", "3631915"))
    player1h2h.substitutions.homeMatches.map(_.id) should equal(List("3684147", "3679045", "3631659", "3643949"))
    player1h2h.substitutions.awayMatches.map(_.id) should equal(List("3690792", "3679046", "3631620", "3643939", "3667022"))

    player2h2h should have (
      Symbol("id") ("355256"),
      Symbol("name") ("Demba Ba"),
      Symbol("totalGoals") (3),
      Symbol("totalBookings") (0),
      Symbol("totalDismissals") (0),
      Symbol("totalSubstitutions") (11)
    )
    player2h2h.substitutions.homeCount should equal(6)
    player2h2h.substitutions.awayCount should equal(5)
  }

  "PaClient" should "load the player head to head information with competition filter" in {
    val (team1, team2) = Await.result(
      StubClient.playerHead2Head("300448", "494151", LocalDate.of(2013, 11, 3), LocalDate.of(2014, 2, 4), "100"),
      10.seconds
    )

    team1 should have (
      Symbol("id") ("300448"),
      Symbol("name") ("Abou Diaby"),
      Symbol("totalGoals") (0),
      Symbol("totalBookings") (0),
      Symbol("totalDismissals") (0),
      Symbol("totalSubstitutions") (0)
    )
    team2 should have (
      Symbol("id") ("494151"),
      Symbol("name") ("Adnan Januzaj"),
      Symbol("totalGoals") (1),
      Symbol("totalBookings") (3),
      Symbol("totalDismissals") (0),
      Symbol("totalSubstitutions") (7)
    )
  }
}
