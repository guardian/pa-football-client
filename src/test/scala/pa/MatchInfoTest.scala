package pa

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MatchInfoTest extends FlatSpec with ShouldMatchers {
  "PaClient" should "load matchInfo" in {
    val theMatch = Await.result(StubClient.matchInfo("3695171"), 1.second)

    theMatch should have (
      'id ("3695171"),
      'round (Some(Round("7", "Semi-Final Second Leg"))),
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
      'comments (Some("(Sunderland win 2-1 on penalties)"))
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
