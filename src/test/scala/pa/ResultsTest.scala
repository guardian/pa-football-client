package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ResultsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load results" in {
    val matches = Await.result(StubClient.results("100", new DateMidnight(2010, 8, 1)), 1.second)

    matches.size should be(780)

    matches(3) should be(
      Result(
        "3517772",
        new DateTime(2012, 8, 25, 15, 0, 0, 0),
        Some(Round("1", None)),
        "1",
        reportAvailable = true,
        attendance = Some("36166"),
        homeTeam = MatchDayTeam(
          "19", "Tottenham", Some(1), Some(0), None, Some("Benoit Assou-Ekotto (74)")
        ),
        awayTeam = MatchDayTeam("42", "West Brom", Some(1), Some(0), None, Some("James Morrison (90 +0:21)")),
        referee = Some(Official("100924", "Mike Dean")),
        venue = Some(Venue("61", "White Hart Lane")),
        comments = None
      )
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