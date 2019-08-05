package pa

import java.time.{LocalDate, ZoneId, ZonedDateTime}

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class TeamResultsTest extends FlatSpec with Matchers {

  "PaClient" should "load team results" in {
    val results = Await.result(StubClient.teamResults("19", LocalDate.of(2013, 10, 11),  LocalDate.of(2014, 1, 30)), 10.seconds)

    results.size should be(23)
    results(0) should have (
      'id ("3632243"),
      'date (ZonedDateTime.of(2014, 1, 29, 19, 45, 0, 0, ZoneId.of("Europe/London"))),
      'round (Round("1", Some("League"))),
      'leg ("1"),
      'reportAvailable (true),
      'attendance (Some("36071")),
      'referee (Some(Official("186049" ,"Andre Marriner"))),
      'venue (Some(Venue("61", "White Hart Lane"))),
      'comments (None)
    )
    results(0).homeTeam should have(
      'id ("19"),
      'name ("Tottenham Hotspur"),
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
      'scorers (Some("Sergio Aguero (15),Yaya Toure (51 Pen),Edin Dzeko (53),Stevan Jovetic (78),Vincent Kompany (89)"))
    )
  }
}
