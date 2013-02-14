package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTimeZone, DateTime}

class EAIndexTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load a player's EAIndex" in {
    val competitionId = "100"
    val startDate = new DateTime(2012, 8, 1, 0, 0, 0, 0)
    val endDate = new DateTime(2013, 5, 31, 0, 0, 0, 0)

    val eaIndex = StubClient.eaIndex(competitionId, startDate, endDate)

    val playerIndex = eaIndex.find(_.id == "284085")

    playerIndex should be ('defined)

    playerIndex map { player =>
      player.name should be ("Arouna Kone")
      player.height should be ("1.83 Metres")
      player.weight should be ("74 Kilograms")
      player.dateOfBirth should be (new DateTime(1983, 11, 11, 0, 0, 0, 0))
      player.age should be (29)
      player.nationality should be ("Ivorian")
      player.teams.size should be (1)
      player.teams.headOption map { team =>
        team.team.id should be ("68")
        team.team.name should be ("Wigan")
        team.onLoan should be (false)
        team.squadNumber should be (Some(2))
      }
      player.position should be ("Striker")
      player.matches.size should be (22)
      player.matches.headOption map { _match =>
        _match.matchID should be ("3519478")
        _match.date should be (new DateTime(2013, 2, 9, 0, 0, 0, 0))
        _match.index should be (6)
        _match.minutesOnPitch should be (36)
        _match.allGoals should be (0)
        _match.ownGoals should be (0)
        _match.dismissals should be (0)
        _match.bookings should be (0)
        _match.shotsOnTarget should be (1)
        _match.shotsOffTarget should be (0)
        _match.fouls should be (1)
        _match.tacklesLost should be (1)
        _match.tacklesWon should be (0)
        _match.clearances should be (0)
        _match.interceptions should be (0)
        _match.saves should be (0)
        _match.blocks should be (0)
        _match.passes should be (7)
        _match.dribbles should be (1)
        _match.crosses should be (0)
        _match.lastUpdated should be (new DateTime(2013, 2, 14, 10, 2, 44, 0))
      }
    }
  }
}
