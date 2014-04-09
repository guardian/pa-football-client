package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TeamSquadTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load team's squad" in {
    val squadMembers = Await.result(
      StubClient.squad("4"),
      1.second
    )

    squadMembers(0) should have(
      'playerId ("496967"),
      'name ("Andreas Christensen"),
      'squadNumber (None),
      'startDate (new DateMidnight(2013, 5, 19)),
      'endDate (None),
      'onLoan (false)
    )
    squadMembers(9) should have(
      'playerId ("41948"),
      'name ("Ashley Cole"),
      'squadNumber (Some("3")),
      'startDate (new DateMidnight(2006, 9, 1)),
      'endDate (None),
      'onLoan (false)
    )
  }
}
