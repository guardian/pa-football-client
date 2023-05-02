package pa

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class TeamSquadTest extends FlatSpec with Matchers {
  "PaClient" should "load team's squad" in {
    val squadMembers = Await.result(
      StubClient.squad("4"),
      10.seconds
    )

    squadMembers(0) should have(
      Symbol("playerId") ("38930"),
      Symbol("name") ("John Terry"),
      Symbol("squadNumber") (Some("26")),
      Symbol("startDate") (LocalDate.of(1998, 8, 1)),
      Symbol("endDate") (None),
      Symbol("onLoan") (false)
    )
    squadMembers(9) should have(
      Symbol("playerId") ("370846"),
      Symbol("name") ("Marko Marin"),
      Symbol("squadNumber") (None),
      Symbol("startDate") (LocalDate.of(2012, 7, 1)),
      Symbol("endDate") (None),
      Symbol("onLoan") (false)
    )
  }
}
