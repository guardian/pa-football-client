package pa

import java.time.LocalDate

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class CompetitionTeamsTest extends FlatSpec with Matchers {
  "PaClient" should "load the competition's teams" in {
    val teams = Await.result(
      StubClient.teams("100", LocalDate.of(2015, 12, 5), LocalDate.of(2016, 2, 4)),
      10.seconds
    )

    teams should have length 20
    teams(1) should have (
      'id ("1006"),
      'name ("Arsenal")
    )
    teams(6) should have (
      'id ("29"),
      'name ("Leicester")
    )
  }
}
