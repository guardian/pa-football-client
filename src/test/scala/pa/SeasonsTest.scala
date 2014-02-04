package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{Interval, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class SeasonsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load the list of seasons" in {

    val competitions = Await.result(StubClient.competitions, 1.second)

    competitions should contain (Season("100", "785", "Barclays Premier League 13/14",
      new DateMidnight(2013,6,1), new DateMidnight(2014,5,31)))

    competitions should contain (Season("625", "467", "German Bundesliga 12/13",
      new DateMidnight(2012,8,1), new DateMidnight(2013,5,31)))

    competitions(0).interval should be(new Interval(new DateMidnight(2013,6,1), new DateMidnight(2014,5,31)))
    competitions(1).interval should be(new Interval(new DateMidnight(2012,8,1), new DateMidnight(2013,5,31)))
  }
}
