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

    competitions should contain (Season("100", "Barclays Premier League 10/11",
      new DateMidnight(2010,8,1), new DateMidnight(2011,5,31)))

    competitions should contain (Season("794", "ABSA Premier League 10/11",
      new DateMidnight(2010,8,1), new DateMidnight(2011,5,31)))

    competitions(0).interval should be(new Interval(new DateMidnight(2010,8,1), new DateMidnight(2011,5,31)))
    competitions(1).interval should be(new Interval(new DateMidnight(2010,8,1), new DateMidnight(2011,5,31)))
  }
}
