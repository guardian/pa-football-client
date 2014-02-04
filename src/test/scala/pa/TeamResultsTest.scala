package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class TeamResultsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load team results" in {
    val results = Await.result(StubClient.teamResults("19", new DateMidnight(2013, 10, 11)), 1.second)

    results.size should be(23)
    results(0) should have (
      'id ("3632243"),
      'date (new DateTime(2014, 1, 29, 19, 45, 0, 0)),
      'round (Some(Round("1", Some("League")))),
      'leg ("1"),
      'reportAvailable (true),
      'attendance (Some("36071")),
      'referee (None),
      'venue (Some(Venue("61", "White Hart Lane"))),
      'comments (None)
    )
    results(0).homeTeam should have(
      'id ("19"),
      'name ("Spurs"),
      'score (Some(1)),
      'htScore (Some(0)),
      'aggregateScore (None),
      'scorers (Some("Etienne Capoue (59)"))
    )
    results(0).awayTeam should have(
      'id ("11"),
      'name ("Man City"),
      'score (Some(5)),
      'htScore (Some(1)),
      'aggregateScore (None),
      'scorers (Some("Sergio Aguero (15),Yaya Toure (51 pen),Edin Dzeko (53),Stevan Jovetic (78),Vincent Kompany (89)"))
    )
  }
}
