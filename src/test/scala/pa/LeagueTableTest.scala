package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.DateMidnight

class LeagueTableTest extends FlatSpec with ShouldMatchers{

  "PaClient" should "load a League Table" in {
    val List(first, second) = StubClient.leagueTable("100", new DateMidnight(2011, 8, 27)) take 2

    first.stageNumber should be("1")
    first.round should be(Some(Round("1",None)))
    val manUtd = LeagueTeam(
      id = "12",
      name = "Man Utd",
      rank = 1,
      played = 3,
      won = 3,
      drawn = 0,
      lost = 0,
      goalsFor = 13,
      goalsAgainst = 3,
      goalDifference = 10,
      points = 9
    )
    first.team should be(manUtd)
    second.team.name should be("Man City")

  }

  it should "load a League Table with negative Goal Difference" in {
    val List(entry) = StubClient.leagueTable("100", new DateMidnight(2011, 8, 28)) filter (_.team.name == "QPR") take 1

    entry.team.goalDifference should be(-5)
  }
}
