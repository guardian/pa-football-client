package pa

import java.time.{LocalDate, LocalDateTime, ZoneId}

import org.scalatest.{FlatSpec, Matchers, OptionValues}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class ResultsTest extends FlatSpec with Matchers with OptionValues {

  "PaClient" should "load results" in {
    val matches = Await.result(StubClient.results("100", LocalDate.of(2016, 4, 1)), 10.seconds)

    matches.size should be(77)
    val result = matches(3)
    
    result should have (
      Symbol("id") ("3834731"),
      Symbol("date") (LocalDateTime.of(2016, 5, 15, 15, 0, 0, 0).atZone(ZoneId.of("Europe/London"))),
      Symbol("stage") (Stage("1")),
      Symbol("round") (Round("1", Some("League"))),
      Symbol("leg") ("1"),
      Symbol("reportAvailable") (true),
      Symbol("attendance") (Some("36691")),
      Symbol("comments") (None)
    )
    result.homeTeam should have (
      Symbol("id") ("8"),
      Symbol("name") ("Everton"),
      Symbol("score") (Some(3)),
      Symbol("htScore") (Some(2)),
      Symbol("aggregateScore") (None),
      Symbol("scorers") (Some("James McCarthy (19),Leighton Baines (44 Pen),Kevin Mirallas (48)"))
    )
    result.awayTeam should have (
      Symbol("id") ("14"),
      Symbol("name") ("Norwich"),
      Symbol("score") (Some(0)),
      Symbol("htScore") (Some(0)),
      Symbol("aggregateScore") (None),
      Symbol("scorers") (None)
    )
    result.referee.value should have (
      Symbol("id") ("182780"),
      Symbol("name") ("Lee Mason")
    )
    result.venue.value should have (
      Symbol("id") ("71"),
      Symbol("name") ("Goodison Park")
    )
  }

  it should "work with an end date" in {
    val matches = Await.result(StubClient.results("100", LocalDate.of(2014, 8, 23), LocalDate.of(2014, 9, 1)), 10.seconds)

    matches(0).homeTeam.name should be ("Leicester")
  }

  it should "get results across all competitions from a start date" in {
    val results = Await.result(StubClient.results(LocalDate.of(2016, 3, 23)), 10.seconds)
    
    results.size should be (1329)
    
    results(0).id should be ("3888450")
    results(1).id should be ("3888451")
  }
  
    it should "get results across all competitions from a start date to an end date" in {
    val results = Await.result(StubClient.results(LocalDate.of(2016, 3, 23), LocalDate.of(2016, 5, 23)), 10.seconds)
    
    results.size should be (1224)
    
    results(0).id should be ("3908357")
    results(1).id should be ("3921045")
  }
  
}