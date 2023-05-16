package pa

import java.time.{LocalDate, LocalDateTime, ZoneId}


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class TeamResultsTest extends AnyFlatSpec with Matchers {

  "PaClient" should "load team results" in {
    val results = Await.result(StubClient.teamResults("19", LocalDate.of(2013, 10, 11),  LocalDate.of(2014, 1, 30)), 10.seconds)

    results.size should be(23)
    results(0) should have (
      Symbol("id") ("3632243"),
      Symbol("date") (LocalDateTime.of(2014, 1, 29, 19, 45, 0, 0).atZone(ZoneId.of("Europe/London"))),
      Symbol("round") (Round("1", Some("League"))),
      Symbol("leg") ("1"),
      Symbol("reportAvailable") (true),
      Symbol("attendance") (Some("36071")),
      Symbol("referee") (Some(Official("186049" ,"Andre Marriner"))),
      Symbol("venue") (Some(Venue("61", "White Hart Lane"))),
      Symbol("comments") (None)
    )
    results(0).homeTeam should have(
      Symbol("id") ("19"),
      Symbol("name") ("Tottenham Hotspur"),
      Symbol("score") (Some(1)),
      Symbol("htScore") (Some(0)),
      Symbol("aggregateScore") (None),
      Symbol("scorers") (Some("Etienne Capoue (59)"))
    )
    results(0).awayTeam should have(
      Symbol("id") ("11"),
      Symbol("name") ("Man City"),
      Symbol("score") (Some(5)),
      Symbol("htScore") (Some(1)),
      Symbol("aggregateScore") (None),
      Symbol("scorers") (Some("Sergio Aguero (15),Yaya Toure (51 Pen),Edin Dzeko (53),Stevan Jovetic (78),Vincent Kompany (89)"))
    )
  }
}
