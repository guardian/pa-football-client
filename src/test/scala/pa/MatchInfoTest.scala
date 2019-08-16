package pa

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class MatchInfoTest extends FlatSpec with Matchers {
  "PaClient" should "load matchInfo" in {
    val theMatch = Await.result(StubClient.matchInfo("3695171"), 10.seconds)

    theMatch should have (
      'id ("3695171"),
      'round (Round("7", "Semi-Final Second Leg")),
      'leg ("2"),
      'liveMatch (false),
      'result (true),
      'previewAvailable (true),
      'reportAvailable (true),
      'lineupsAvailable (true),
      'matchStatus ("FT"),
      'attendance (None),
      'referee (Some(Official("182780", "Lee Mason"))),
      'venue (Some(Venue("114", "Old Trafford"))),
      'comments (None)
    )

    theMatch.homeTeam should have (
      'id ("12"),
      'name ("Man Utd"),
      'score (Some(2)),
      'htScore (Some(1)),
      'aggregateScore (Some(3)),
      'scorers (Some("Jonny Evans (37),Javier Hernandez (120 +0:28)"))
    )

    theMatch.awayTeam should have (
      'id ("39"),
      'name ("Sunderland"),
      'score (Some(1)),
      'htScore (Some(0)),
      'aggregateScore (Some(3)),
      'scorers (Some("Phil Bardsley (119)"))
    )
  }
}
