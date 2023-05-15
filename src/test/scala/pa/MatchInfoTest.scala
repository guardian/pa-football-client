package pa


import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MatchInfoTest extends AnyFlatSpec with Matchers {
  "PaClient" should "load matchInfo" in {
    val theMatch = Await.result(StubClient.matchInfo("3695171"), 10.seconds)

    theMatch should have (
      Symbol("id") ("3695171"),
      Symbol("round") (Round("7", "Semi-Final Second Leg")),
      Symbol("leg") ("2"),
      Symbol("liveMatch") (false),
      Symbol("result") (true),
      Symbol("previewAvailable") (true),
      Symbol("reportAvailable") (true),
      Symbol("lineupsAvailable") (true),
      Symbol("matchStatus") ("FT"),
      Symbol("attendance") (None),
      Symbol("referee") (Some(Official("182780", "Lee Mason"))),
      Symbol("venue") (Some(Venue("114", "Old Trafford"))),
      Symbol("comments") (None)
    )

    theMatch.homeTeam should have (
      Symbol("id") ("12"),
      Symbol("name") ("Man Utd"),
      Symbol("score") (Some(2)),
      Symbol("htScore") (Some(1)),
      Symbol("aggregateScore") (Some(3)),
      Symbol("scorers") (Some("Jonny Evans (37),Javier Hernandez (120 +0:28)"))
    )

    theMatch.awayTeam should have (
      Symbol("id") ("39"),
      Symbol("name") ("Sunderland"),
      Symbol("score") (Some(1)),
      Symbol("htScore") (Some(0)),
      Symbol("aggregateScore") (Some(3)),
      Symbol("scorers") (Some("Phil Bardsley (119)"))
    )
  }
}
