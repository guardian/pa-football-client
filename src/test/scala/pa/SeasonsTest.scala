package pa

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.joda.time.{Interval, LocalDate}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class SeasonsTest extends FlatSpec with Matchers {

  "PaClient" should "load the list of seasons" in {

    val seasons = Await.result(StubClient.competitions, 10.seconds)
    def seasonWithName(name: String) : Season = seasons.collectFirst{ case c if c.name == name => c }.get

    seasons should contain (Season("100", "785", "Barclays Premier League 13/14",
      new LocalDate(2013,6,1), new LocalDate(2014,5,31)))

    seasons should contain (Season("625", "467", "German Bundesliga 12/13",
      new LocalDate(2012,8,1), new LocalDate(2013,5,31)))

    seasonWithName("Barclays Premier League 12/13").interval should be(new Interval(new LocalDate(2012,8,1).toDateTimeAtStartOfDay, new LocalDate(2013,5,31).toDateTimeAtStartOfDay))
    seasonWithName("Barclays Premier League 13/14").interval should be(new Interval(new LocalDate(2013,6,1).toDateTimeAtStartOfDay, new LocalDate(2014,5,31).toDateTimeAtStartOfDay))
  }
}
