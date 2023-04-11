package pa

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class PlayerAppearancesTest extends FlatSpec with Matchers {
  "PaClient" should "load a player's appearances" in {
    val playerAppearances = Await.result(
      StubClient.appearances("237670", LocalDate.of(2013, 9, 4), LocalDate.of(2014, 2, 4)),
      10.seconds
    )

    playerAppearances.playerName should equal ("Emmanuel Adebayor")
    playerAppearances.home should have (
      Symbol("appearances") (5),
      Symbol("started") (5),
      Symbol("substitutedOn") (0),
      Symbol("substitutedOff") (2),
      Symbol("dismissals") (0)
    )
    playerAppearances.away should have (
      Symbol("appearances") (6),
      Symbol("started") (5),
      Symbol("substitutedOn") (1),
      Symbol("substitutedOff") (2),
      Symbol("dismissals") (0)
    )
    playerAppearances.total should have (
      Symbol("appearances") (11),
      Symbol("started") (10),
      Symbol("substitutedOn") (1),
      Symbol("substitutedOff") (4),
      Symbol("dismissals") (0)
    )
  }

  "PaClient" should "load a player's appearances for the specified team" in {
    val playerAppearances = Await.result(
      StubClient.appearances("237670", LocalDate.of(2013, 9, 4), LocalDate.of(2014, 2, 4), "19"),
      10.seconds
    )

    playerAppearances.playerName should equal ("Emmanuel Adebayor")
    playerAppearances.home should have (
      Symbol("appearances") (5),
      Symbol("started") (5),
      Symbol("substitutedOn") (0),
      Symbol("substitutedOff") (2),
      Symbol("dismissals") (0)
    )
    playerAppearances.away should have (
      Symbol("appearances") (6),
      Symbol("started") (5),
      Symbol("substitutedOn") (1),
      Symbol("substitutedOff") (2),
      Symbol("dismissals") (0)
    )
    playerAppearances.total should have (
      Symbol("appearances") (11),
      Symbol("started") (10),
      Symbol("substitutedOn") (1),
      Symbol("substitutedOff") (4),
      Symbol("dismissals") (0)
    )
  }

  "PaClient" should "load a player's appearances for the specified team and competition" in {
    val playerAppearances = Await.result(
      StubClient.appearances("237670", LocalDate.of(2013, 9, 4), LocalDate.of(2014, 2, 4), "19", "100"),
      10.seconds
    )

    playerAppearances.playerName should equal ("Emmanuel Adebayor")
    playerAppearances.home should have (
      Symbol("appearances") (4),
      Symbol("started") (4),
      Symbol("substitutedOn") (0),
      Symbol("substitutedOff") (1),
      Symbol("dismissals") (0)
    )
    playerAppearances.away should have (
      Symbol("appearances") (5),
      Symbol("started") (4),
      Symbol("substitutedOn") (1),
      Symbol("substitutedOff") (2),
      Symbol("dismissals") (0)
    )
    playerAppearances.total should have (
      Symbol("appearances") (9),
      Symbol("started") (8),
      Symbol("substitutedOn") (1),
      Symbol("substitutedOff") (3),
      Symbol("dismissals") (0)
    )
  }
}
