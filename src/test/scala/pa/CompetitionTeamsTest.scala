package pa

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class CompetitionTeamsTest extends FlatSpec with Matchers {
  "PaClient" should "load the competition's teams" in {
    val teams = Await.result(
      StubClient.teams("100", LocalDate.of(2015, 12, 5), LocalDate.of(2016, 2, 4)),
      10.seconds
    )

    teams should have length 20
    teams(1) should have (
      Symbol("id") ("1006"),
      Symbol("name") ("Arsenal")
    )
    teams(6) should have (
      Symbol("id") ("29"),
      Symbol("name") ("Leicester")
    )
  }
}
