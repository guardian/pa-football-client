package pa

import java.time.{LocalDate, LocalDateTime, ZoneId}

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class TeamEventsTest extends FlatSpec with Matchers {

  private val spurs = Team("19", "Tottenham Hotspur")
  private val swansea = Team("65", "Swansea")
  "PaClient" should "load the team events info" in {

    val matches = Await.result(
      StubClient.teamEvents("19", LocalDate.of(2013, 10, 11), LocalDate.of(2014, 1, 24)),
      10.seconds
    )

    val match0 = matches(0)
    match0 should have (
      Symbol("id") ("3684146"),
      Symbol("date") (LocalDateTime.of(2014, 1, 19, 13, 30, 0, 0).atZone(ZoneId.of("Europe/London"))),
      Symbol("competitionId") ("100"),
      Symbol("stage") (1),
      Symbol("round") (1),
      Symbol("leg") (1)
    )
    match0.homeTeam should have (
      Symbol("id") ("65"),
      Symbol("name") ("Swansea"),
      Symbol("score") (1),
      Symbol("htScore") (0),
      Symbol("aggregateScore") (None)
    )
    match0.awayTeam should have (
      Symbol("id") ("19"),
      Symbol("name") ("Tottenham Hotspur"),
      Symbol("score") (3),
      Symbol("htScore") (1),
      Symbol("aggregateScore") (None)
    )
    val events0 = match0.events

    events0.bookings should have length 3
    val booking0 = events0.bookings(0)
    booking0 should have (
      Symbol("eventId") ("18670657"),
      Symbol("normalTime") ("48:31"),
      Symbol("addedTime") ("0:00"),
      Symbol("reason") ("Unsporting behaviour")
    )
    booking0.team should equal (swansea)
    booking0.player should equal(Player("428189", "65", "Jordi Amat"))


    events0.goals should have length 4
    val goal0 = events0.goals(0)
    goal0 should have (
      Symbol("eventId") ("18670511"),
      Symbol("normalTime") ("34:24"),
      Symbol("addedTime") ("0:00"),
      Symbol("ownGoal") (false),
      Symbol("how") (Some("Head")),
      Symbol("whereFrom") (Some("Centre 6 Yard")),
      Symbol("whereTo") (Some("Left Low")),
      Symbol("distanceInYards") (None)
    )
    goal0.team should equal (spurs)
    goal0.player should equal(Player("237670", "19", "Emmanuel Adebayor"))

    events0.substitutions should have length 3
    val sub0 = events0.substitutions(0)
    sub0 should have (
      Symbol("eventId") ("18670659"),
      Symbol("normalTime") ("50:15"),
      Symbol("addedTime") ("0:00"),
      Symbol("reason") (Some("Injury"))
    )
    sub0.team should equal (swansea)
    sub0.playerOff should equal (Player("389451", "65", "Jonjo Shelvey"))
    sub0.playerOn should equal (Player("303737", "65", "Roland Lamah"))

    events0.penalties should have length 0
    events0.dismissals should have length 0
    events0.shootoutPenalties should have length 0

    matches(1).events.penalties should have length 1
    matches(1).events.penalties(0) should have (
      Symbol("eventId") ("18624801"),
      Symbol("normalTime") ("7:48"),
      Symbol("addedTime") ("0:00"),
      Symbol("how") (Some("Left Foot")),
      Symbol("keeperCorrect") (Some(false)),
      Symbol("outcome") ("Missed"),
      Symbol("type") (None),
      Symbol("whereTo") (None)
    )
    matches(1).events.penalties(0).team should equal(Team("5", "Crystal Palace"))
    matches(1).events.penalties(0).player should equal(Player("291350", "5", "Jason Puncheon"))

    matches(8).events.dismissals should have length 1
    matches(8).events.dismissals(0) should have (
      Symbol("eventId") ("18513360"),
      Symbol("normalTime") ("62:42"),
      Symbol("addedTime") ("0:00"),
      Symbol("reason") ("Serious Foul Play")
    )
    matches(8).events.dismissals(0).team should equal(spurs)
    matches(8).events.dismissals(0).player should equal(Player("362826", "19", "Jose Paulo Paulinho"))

    matches(18).events.shootoutPenalties should have length 18
    matches(18).events.shootoutPenalties(0) should have(
      Symbol("eventId") ("18265079"),
      Symbol("how") (Some("Right Foot")),
      Symbol("keeperCorrect") (Some(false)),
      Symbol("outcome") (Some("Scored")),
      Symbol("type") (None),
      Symbol("whereTo") (Some("Left Low"))
    )
    matches(18).events.shootoutPenalties(0).team should equal(spurs)
    matches(18).events.shootoutPenalties(0).player should equal(Player("376065", "19", "Gylfi Sigurdsson"))
  }
}
