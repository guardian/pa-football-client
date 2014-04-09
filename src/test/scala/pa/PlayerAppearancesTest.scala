package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.joda.time.{Interval, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class PlayerAppearancesTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load a player's appearances" in {
    val playerAppearances = Await.result(
      StubClient.appearances("237670", new DateMidnight(2013, 9, 4), new DateMidnight(2014, 2, 4)),
      1.second
    )

    playerAppearances.playerName should equal ("Emmanuel Adebayor")
    playerAppearances.home should have (
      'appearances (5),
      'started (5),
      'substitutedOn (0),
      'substitutedOff (2),
      'dismissals (0)
    )
    playerAppearances.away should have (
      'appearances (6),
      'started (5),
      'substitutedOn (1),
      'substitutedOff (2),
      'dismissals (0)
    )
    playerAppearances.total should have (
      'appearances (11),
      'started (10),
      'substitutedOn (1),
      'substitutedOff (4),
      'dismissals (0)
    )
  }

  "PaClient" should "load a player's appearances for the specified team" in {
    val playerAppearances = Await.result(
      StubClient.appearances("237670", new DateMidnight(2013, 9, 4), new DateMidnight(2014, 2, 4), "19"),
      1.second
    )

    playerAppearances.playerName should equal ("Emmanuel Adebayor")
    playerAppearances.home should have (
      'appearances (5),
      'started (5),
      'substitutedOn (0),
      'substitutedOff (2),
      'dismissals (0)
    )
    playerAppearances.away should have (
      'appearances (6),
      'started (5),
      'substitutedOn (1),
      'substitutedOff (2),
      'dismissals (0)
    )
    playerAppearances.total should have (
      'appearances (10),
      'started (9),
      'substitutedOn (1),
      'substitutedOff (4),
      'dismissals (0)
    )
  }

  "PaClient" should "load a player's appearances for the specified team and competition" in {
    val playerAppearances = Await.result(
      StubClient.appearances("237670", new DateMidnight(2013, 9, 4), new DateMidnight(2014, 2, 4), "19", "100"),
      1.second
    )

    playerAppearances.playerName should equal ("Emmanuel Adebayor")
    playerAppearances.home should have (
      'appearances (4),
      'started (4),
      'substitutedOn (0),
      'substitutedOff (1),
      'dismissals (0)
    )
    playerAppearances.away should have (
      'appearances (5),
      'started (4),
      'substitutedOn (1),
      'substitutedOff (2),
      'dismissals (0)
    )
    playerAppearances.total should have (
      'appearances (9),
      'started (8),
      'substitutedOn (1),
      'substitutedOff (3),
      'dismissals (0)
    )
  }
}
