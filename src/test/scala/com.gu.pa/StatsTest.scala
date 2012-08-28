package com.gu.pa

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class StatsTest extends FlatSpec with ShouldMatchers {

  "PaClient" should "load match stats" in {

    val stats = StubClient.matchStats("3409881").get

    stats.homeTeam.corners should be(9)

    stats.awayTeam.shotsOnTarget should be(7)

    stats.awayPossession should be (51)
  }

  it should "load empty match stats" in {
    StubClient.matchStats("3283333") should be (None)
  }
}
