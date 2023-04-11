package pa

import java.time.LocalDate


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TeamSquadTest extends AnyFlatSpec with Matchers {
  "PaClient" should "load team's squad" in {
    val squadMembers = Await.result(
      StubClient.squad("4"),
      10.seconds
    )

    squadMembers(0) should have(
      'playerId ("38930"),
      'name ("John Terry"),
      'squadNumber (Some("26")),
      'startDate (LocalDate.of(1998, 8, 1)),
      'endDate (None),
      'onLoan (false)
    )
    squadMembers(9) should have(
      'playerId ("370846"),
      'name ("Marko Marin"),
      'squadNumber (None),
      'startDate (LocalDate.of(2012, 7, 1)),
      'endDate (None),
      'onLoan (false)
    )
  }
}
