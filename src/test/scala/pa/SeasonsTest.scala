package pa

import java.time.{LocalDate, LocalDateTime, ZoneOffset}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class SeasonsTest extends FlatSpec with Matchers {

  implicit val localDateOrdering: Ordering[LocalDateTime] = Ordering.by(_.toEpochSecond(ZoneOffset.UTC))

  "PaClient" should "load the list of seasons" in {

    val seasons = Await.result(StubClient.competitions, 10.seconds)
    def seasonWithName(name: String) : Season = seasons.collectFirst{ case c if c.name == name => c }.get

    seasons should contain (Season("100", "785", "Barclays Premier League 13/14",
      LocalDate.of(2013,6,1), LocalDate.of(2014,5,31)))

    seasons should contain (Season("625", "467", "German Bundesliga 12/13",
        LocalDate.of(2012,8,1),   LocalDate.of(2013,5,31)))

    val now = LocalDateTime.now()
    
    //seasonWithName("Barclays Premier League 12/13").interval.toString() should be(MangoRange.closed(LocalDate.of(2012,8,1).atStartOfDay(), LocalDate.of(2013,5,31).atStartOfDay).toString())
    seasonWithName("Barclays Premier League 12/13").startDate should be(LocalDate.of(2012,8,1))
    seasonWithName("Barclays Premier League 12/13").endDate should be(LocalDate.of(2013,5,31))
    seasonWithName("Barclays Premier League 13/14").startDate should be(LocalDate.of(2013,6,1))
    seasonWithName("Barclays Premier League 13/14").endDate should be(LocalDate.of(2014,5,31))
  }
}
