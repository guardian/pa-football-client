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
      Symbol("fullName") ("Emmanuel Adebayor"),
      Symbol("height") (Some("1.9 Metres")),
      Symbol("weight") (Some("75 Kilograms")),
      Symbol("dob") (Some(LocalDate.of(1984, 2, 26))),
      Symbol("age") (Some("32")),
      Symbol("nationality") (Some("Togolese")),
      Symbol("position") (Some("Striker"))
    )
  }
}
