package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.joda.time.DateMidnight
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class LeagueTableTest extends FlatSpec with ShouldMatchers{

  "PaClient" should "load a League Table" in {
    val List(first, second) = Await.result(StubClient.leagueTable("100", new DateMidnight(2011, 8, 27)), 1.second) take 2

    first.stageNumber should be("1")
    first.round should be(Round("1",None))
    val manUtd = LeagueTeam(
      id = "12",
      name = "Man Utd",
      rank = 1,
      total = LeagueStats(played = 3, won = 3, drawn = 0, lost = 0, goalsFor = 13, goalsAgainst = 3),
      home = LeagueStats(played = 2, won = 2, drawn = 0, lost = 0, goalsFor = 11, goalsAgainst = 2),
      away = LeagueStats(played = 1, won = 1, drawn = 0, lost = 0, goalsFor = 2, goalsAgainst = 1),
      goalDifference = 10,
      points = 9
    )
    first.team should be(manUtd)
    second.team.name should be("Man City")

  }

  it should "load a League Table with negative Goal Difference" in {
    val List(entry) = Await.result(StubClient.leagueTable("100", new DateMidnight(2011, 8, 28)), 1.second) filter (_.team.name == "QPR") take 1

    entry.team.goalDifference should be(-5)
  }
}
