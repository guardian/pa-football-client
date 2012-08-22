package pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTime, DateMidnight}

class MatchDayTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load a match day" in {
    val matches = StubClient.matchDay("100", new DateMidnight(2011, 8, 27))

    matches.size should be (6)

    matches(3) should be(
      MatchDay(
        "3407349",
        new DateMidnight(2011, 8, 27),
        "15:00",
        "1",
        "1",
        false,
        true,
        true,
        true,
        true,
        "FT",
        "41765"
        ,MatchDayTeam("4","Chelsea",3,1,None,Some("Jose Bosingwa (6),Frank Lampard (82 Pen),Juan Mata (90 +10:03)"))
        ,MatchDayTeam("14","Norwich",1,0,None,Some("Grant Holt (63)")),
        new Official("410888", "Mike Jones"),
        new Venue("511", "Stamford Bridge")
      )
    )

    matches(3).kickOffTime should be (new DateTime(2011, 8, 27, 15, 0, 0, 0))
  }
}
