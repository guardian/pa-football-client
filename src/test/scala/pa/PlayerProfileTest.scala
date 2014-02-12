package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class PlayerProfileTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load player profile info" in {
    val playerProfile = Await.result(
      StubClient.playerProfile("237670"),
      1.second
    )

    playerProfile should have (
      'fullName ("Emmanuel Adebayor"),
      'height (Some("1.9 Metres")),
      'weight (Some("75 Kilograms")),
      'dob (Some(new DateMidnight(1984, 2, 26))),
      'age (Some("29")),
      'nationality (Some("Togolese")),
      'position (Some("Striker"))
    )
  }
}
