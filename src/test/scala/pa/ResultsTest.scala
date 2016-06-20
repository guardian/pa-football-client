package pa

import org.scalatest.{OptionValues, FlatSpec, ShouldMatchers}
import org.joda.time.{DateTime, LocalDate}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ResultsTest extends FlatSpec with ShouldMatchers with OptionValues {

  "PaClient" should "load results" in {
    val matches = Await.result(StubClient.results("100", new LocalDate(2016, 4, 1)), 10.seconds)

    matches.size should be(77)
    val result = matches(3)
    
    result should have (
      'id ("3834731"),
      'date (new DateTime(2016, 5, 15, 15, 0, 0, 0)),
      'stage (Stage("1")),
      'round (Round("1", Some("League"))),
      'leg ("1"),
      'reportAvailable (true),
      'attendance (Some("36691")),
      'comments (None)
    )
    result.homeTeam should have (
      'id ("8"),
      'name ("Everton"),
      'score (Some(3)),
      'htScore (Some(2)),
      'aggregateScore (None),
      'scorers (Some("James McCarthy (19),Leighton Baines (44 Pen),Kevin Mirallas (48)"))
    )
    result.awayTeam should have (
      'id ("14"),
      'name ("Norwich"),
      'score (Some(0)),
      'htScore (Some(0)),
      'aggregateScore (None),
      'scorers (None)
    )
    result.referee.value should have (
      'id ("182780"),
      'name ("Lee Mason")
    )
    result.venue.value should have (
      'id ("71"),
      'name ("Goodison Park")
    )
  }

  it should "work with an end date" in {
    val matches = Await.result(StubClient.results("100", new LocalDate(2014, 8, 23), new LocalDate(2014, 9, 1)), 10.seconds)

    matches(0).homeTeam.name should be ("Leicester")
  }

  it should "get results across all competitions from a start date" in {
    val results = Await.result(StubClient.results(new LocalDate(2016, 3, 23)), 10.seconds)
    
    results.size should be (1329)
    
    results(0).id should be ("3888450")
    results(1).id should be ("3888451")
  }
  
    it should "get results across all competitions from a start date to an end date" in {
    val results = Await.result(StubClient.results(new LocalDate(2016, 3, 23), new LocalDate(2016, 5, 23)), 10.seconds)
    
    results.size should be (1224)
    
    results(0).id should be ("3908357")
    results(1).id should be ("3921045")
  }
  
}