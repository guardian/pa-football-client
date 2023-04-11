package pa

import java.time.LocalDate


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerProfileTest extends AnyFlatSpec with Matchers {
  "PaClient" should "load player profile info" in {
    val playerProfile = Await.result(
      StubClient.playerProfile("237670"),
      10.seconds
    )

    playerProfile should have (
      'fullName ("Emmanuel Adebayor"),
      'height (Some("1.9 Metres")),
      'weight (Some("75 Kilograms")),
      'dob (Some(LocalDate.of(1984, 2, 26))),
      'age (Some("32")),
      'nationality (Some("Togolese")),
      'position (Some("Striker"))
    )
  }
}
