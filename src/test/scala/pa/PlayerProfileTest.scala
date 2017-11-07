package pa

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.joda.time.{DateTime, LocalDate}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class PlayerProfileTest extends FlatSpec with Matchers {
  "PaClient" should "load player profile info" in {
    val playerProfile = Await.result(
      StubClient.playerProfile("237670"),
      10.seconds
    )

    playerProfile should have (
      'fullName ("Emmanuel Adebayor"),
      'height (Some("1.9 Metres")),
      'weight (Some("75 Kilograms")),
      'dob (Some(new LocalDate(1984, 2, 26))),
      'age (Some("32")),
      'nationality (Some("Togolese")),
      'position (Some("Striker"))
    )
  }
}
