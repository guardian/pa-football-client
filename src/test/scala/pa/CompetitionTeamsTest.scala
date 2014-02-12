package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.concurrent.Await
import org.joda.time.DateMidnight
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class CompetitionTeamsTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load the competition's teams" in {
    val teams = Await.result(
      StubClient.teams("100", new DateMidnight(2013, 12, 5), new DateMidnight(2014, 2, 4)),
      1.second
    )

    teams should have length 20
    teams(0) should have (
      'id ("1006"),
      'name ("Arsenal")
    )
    teams(14) should have (
      'id ("19"),
      'name ("Spurs")
    )
  }
}
