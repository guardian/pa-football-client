package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.joda.time.{DateTime, LocalDate}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TeamSquadTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load team's squad" in {
    val squadMembers = Await.result(
      StubClient.squad("4"),
      10.seconds
    )

    squadMembers(0) should have(
      'playerId ("38930"),
      'name ("John Terry"),
      'squadNumber (Some("26")),
      'startDate (new LocalDate(1998, 8, 1)),
      'endDate (None),
      'onLoan (false)
    )
    squadMembers(9) should have(
      'playerId ("370846"),
      'name ("Marko Marin"),
      'squadNumber (None),
      'startDate (new LocalDate(2012, 7, 1)),
      'endDate (None),
      'onLoan (false)
    )
  }
}
