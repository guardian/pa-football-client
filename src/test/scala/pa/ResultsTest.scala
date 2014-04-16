package pa

import org.scalatest.{OptionValues, FlatSpec, ShouldMatchers}
import org.joda.time.{DateTime, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ResultsTest extends FlatSpec with ShouldMatchers with OptionValues {

  "PaClient" should "load results" in {
    val matches = Await.result(StubClient.results("100", new DateMidnight(2010, 8, 1)), 1.second)

    matches.size should be(780)
    val result = matches(3)
    
    result should have (
      'id ("3517772"),
      'date (new DateTime(2012, 8, 25, 15, 0, 0, 0)),
      'stage (Stage("1")),
      'round (Round("1", Some("League"))),
      'leg ("1"),
      'reportAvailable (true),
      'attendance (Some("36166")),
      'comments (None)
    )
    result.homeTeam should have (
      'id ("19"),
      'name ("Tottenham"),
      'score (Some(1)),
      'htScore (Some(0)),
      'aggregateScore (None),
      'scorers (Some("Benoit Assou-Ekotto (74)"))
    )
    result.awayTeam should have (
      'id ("42"),
      'name ("West Brom"),
      'score (Some(1)),
      'htScore (Some(0)),
      'aggregateScore (None),
      'scorers (Some("James Morrison (90 +0:21)"))
    )
    result.referee.value should have (
      'id ("100924"),
      'name ("Mike Dean")
    )
    result.venue.value should have (
      'id ("61"),
      'name ("White Hart Lane")
    )
  }

  it should "work with an end date" in {
    val matches = Await.result(StubClient.results("100", new DateMidnight(2012, 8, 23), new DateMidnight(2012, 9, 1)), 1.second)

    matches(0).homeTeam.name should be ("Liverpool")
  }

  it should "load comments" in {

    val theMatch = Await.result(StubClient.results("100", new DateMidnight(2010, 8, 1)), 1.second).find(_.id == "3528299").get

    theMatch.comments should be (Some("Simple comment"))
  }
  
  it should "get results across all competitions from a start date" in {
    val results = Await.result(StubClient.results(new DateMidnight(2012, 8, 23)), 1.second)
    
    results.size should be (2)
    
    results(0).id should be ("3555270")
    results(1).id should be ("3553709")
  }
  
    it should "get results across all competitions from a start date to an end date" in {
    val results = Await.result(StubClient.results(new DateMidnight(2012, 8, 23), new DateMidnight(2012, 9, 23)), 1.second)
    
    results.size should be (2)
    
    results(0).id should be ("3518024")
    results(1).id should be ("3518028")
  }
  
}