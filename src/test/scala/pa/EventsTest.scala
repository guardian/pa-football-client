package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class EventsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load a match" in {

    val theMatch = StubClient.matchEvents("3507403").get

    theMatch.homeTeam should be (Team("9", "Liverpool"))
    theMatch.awayTeam should be (Team("4", "Chelsea"))

    theMatch.homeTeamScore should be (3)

    theMatch.awayTeamScore should be (1)

    theMatch.homeTeamGoals(0).players(0).name should be ("Jordan Henderson")
  }

  "PaClient" should "parse a match's parameters correctly" in {
    val theMatch = StubClient.matchEvents("3519426").get

    val maybeEvent = theMatch.events.find(_.id == Some("17175388"))

    maybeEvent should be ('isDefined)

    val theEvent = maybeEvent.get

    theEvent.eventType should be ("free kick")
    theEvent.matchTime should be (Some("(19)"))
    theEvent.eventTime should be (Some("19"))
    theEvent.players(0) should be (Player("283379", "14", "Robert Snodgrass"))
    theEvent.reason should be (None)
    theEvent.how should be (Some("Left Foot"))
    theEvent.whereFrom should be (Some("Right Wing"))
    theEvent.whereTo should be (None)
    theEvent.typ should be (Some("Direct"))
    theEvent.distance should be (None)
    theEvent.outcome should be (Some("Cross"))
  }

  it should "parse those dodgy matches with no events" in {
    StubClient.matchEvents("3304257") should be (None)
  }
}
