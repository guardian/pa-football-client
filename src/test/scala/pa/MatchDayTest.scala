package pa

import java.time.{LocalDate, LocalDateTime, ZoneId}

import org.scalatest.OptionValues

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MatchDayTest extends AnyFlatSpec with Matchers with OptionValues {

  "PaClient" should "load a match day" in {
    val matches = Await.result(StubClient.matchDay("100",  LocalDate.of(2014, 8, 23)), 10.seconds)

    matches.size should be (6)
    val matchDay = matches(5)
    
    matchDay should have(
      Symbol("id") ("3733674"),
      Symbol("date") (LocalDateTime.of(2014, 8, 23, 17, 30, 0, 0).atZone(ZoneId.of("Europe/London"))),
      Symbol("competition") (None),
      Symbol("stage") (Stage("1")),
      Symbol("round") (Round("1", Some("League"))),
      Symbol("leg") ("1"),
      Symbol("liveMatch") (false),
      Symbol("result") (true),
      Symbol("previewAvailable") (true),
      Symbol("reportAvailable") (true),
      Symbol("lineupsAvailable") (true),
      Symbol("matchStatus") ("FT"),
      Symbol("attendance") (Some("39490")),
      Symbol("comments") (None)
    )
    matchDay.homeTeam should have (
      Symbol("id") ("8"),
      Symbol("name") ("Everton"),
      Symbol("score") (Some(2)),
      Symbol("htScore") (Some(2)),
      Symbol("aggregateScore") (None),
      Symbol("scorers") (Some("Seamus Coleman (19),Steven Naismith (45)"))
    )
    matchDay.awayTeam should have (
      Symbol("id") ("1006"),
      Symbol("name") ("Arsenal"),
      Symbol("score") (Some(2)),
      Symbol("htScore") (Some(0)),
      Symbol("aggregateScore") (None),
      Symbol("scorers") (Some("Aaron Ramsey (83),Olivier Giroud (90)"))
    )
    matchDay.referee.value should have (
      Symbol("id") ("233470"),
      Symbol("name") ("Kevin Friend")
    )
    matchDay.venue.value should have (
      Symbol("id") ("71"),
      Symbol("name") ("Goodison Park")
    )
  }

  it should "parse a day that only has a single game" in {

    val matches = Await.result(StubClient.matchDay("100", LocalDate.of(2014, 8, 25)), 10.seconds)

    matches.size should be (1)

    matches(0).id should be ("3744626")
  }

  it should "handle days with no data" in {
    try {
      Await.result(StubClient.matchDay("100", LocalDate.of(2014, 8, 1)), 10.seconds)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }
  }

  it should "parse match with missing referee" in {

    val matches = Await.result(StubClient.matchDay("750", LocalDate.of(2016, 6, 25)), 10.seconds)

    matches(0).referee should be (None)
  }

  it should "parse match with complex round/stage" in {

    val matches = Await.result(StubClient.matchDay("101", LocalDate.of(2016, 5, 28)), 10.seconds)

    val matchDay = matches(0)
    matchDay.round should be (Round("2", Some("Play-Offs Final")))
    matchDay.stage should be (Stage("2"))
    matchDay.leg should be ("1")
  }

  it should "parse match without half time score" in {

    val matches = Await.result(StubClient.matchDay("750", LocalDate.of(2016, 6, 25)), 10.seconds)

    matches(0).awayTeam.htScore should be (None)
    matches(0).awayTeam.aggregateScore should be (None)

  }

  it should "parse match without attendance" in {

    val matches = Await.result(StubClient.matchDay("750", LocalDate.of(2016, 6, 25)), 10.seconds)

    matches(0).attendance should be (None)
  }


  it should "parse match without score" in {

    val matches = Await.result(StubClient.matchDay("750", LocalDate.of(2016, 6, 25)), 10.seconds)

    matches(0).homeTeam.score should be (None)
  }
  
  it should "load all match days" in {

    val matches = Await.result(StubClient.matchDay(LocalDate.of(2014, 9, 29)), 10.seconds)
    
    matches.size should be (5)

    matches(0).competition should be (Some(Competition("100", "Barclays Premier League 14/15")))

  }
    
}
