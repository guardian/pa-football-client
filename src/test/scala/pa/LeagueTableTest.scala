package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.joda.time.LocalDate
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class LeagueTableTest extends FlatSpec with ShouldMatchers{

  "PaClient" should "load a League Table" in {
    val List(first, second) = Await.result(StubClient.leagueTable("100", new LocalDate(2014, 8, 27)), 10.seconds) take 2

    first.stageNumber should be("1")
    first.round should be(Round("1", "League"))
    val spur = LeagueTeam(
      id = "19",
      name = "Tottenham Hotspur",
      rank = 1,
      total = LeagueStats(played = 2, won = 2, drawn = 0, lost = 0, goalsFor = 5, goalsAgainst = 0),
      home = LeagueStats(played = 1, won = 1, drawn = 0, lost = 0, goalsFor = 4, goalsAgainst = 0),
      away = LeagueStats(played = 1, won = 1, drawn = 0, lost = 0, goalsFor = 1, goalsAgainst = 0),
      goalDifference = 5,
      points = 6
    )
    first.team should be(spur)
    second.team.name should be("Chelsea")

  }

  it should "load a League Table with negative Goal Difference" in {
    val List(entry) = Await.result(StubClient.leagueTable("100", new LocalDate(2014, 8, 28)), 10.seconds) filter (_.team.name == "QPR") take 1

    entry.team.goalDifference should be(-5)
  }
}
