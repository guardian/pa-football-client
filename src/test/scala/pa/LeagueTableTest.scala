package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.DateMidnight

class LeagueTableTest extends FlatSpec with ShouldMatchers{

  "PaClient" should "load a League Table" in {
    val List(first, second) = StubClient.leagueTable("100", new DateMidnight(2011, 8, 27))

    first.stageNumber should be("1")
    first.round should be(None)
    val manUtd = LeagueTeam(teamID = "12", teamName = "Man Utd",
      rank = 1, played = 31, won = 24, drawn = 4,
      lost = 3, goalsFor = 76, goalsAgainst = 27,
      goalDifference = 49, points = 76
    )
    first.team should be(manUtd)
    second.team.teamName should be("Man City")

  }

  it should "load a League Table with negative Goal Difference" in {
    val List(entry) = StubClient.leagueTable("100", new DateMidnight(2011, 8, 28))

    entry.team.goalDifference should be(-5)
  }
}
