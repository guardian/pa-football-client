package pa

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class TeamCodesTest extends FunSuite with ShouldMatchers {
  test("Can get the mapped 3-letter code for a team") {
    TeamCodes.codeFor(new Team("19", "Spurs")) should equal("TOT")
    TeamCodes.codeFor(new Team("37621", "Costa Rica")) should equal("CRC")
  }

  test("Team with no mapping will use first three chars as team code") {
    TeamCodes.codeFor(new Team("i", "Imaginary team")) should equal("IMA")
  }

  test("Will not use spaces in the 3-letter code") {
    TeamCodes.codeFor(new Team("a", "AC Milan")) should equal("ACM")
  }

  test("If a team name is made of precisely 3 words use the initials") {
    TeamCodes.codeFor(new Team("a", "Nice Team Name")) should equal("NTN")
  }

  test("Team with no mapping will skip certain words when creating 3 letter code") {
    TeamCodes.codeFor(new Team("a", "Real Test")) should equal("TES")
    TeamCodes.codeFor(new Team("b", "Athletico Anothertest")) should equal("ANO")
    TeamCodes.codeFor(new Team("c", "Athletic Supertest")) should equal("SUP")
    TeamCodes.codeFor(new Team("d", "FC Megatest")) should equal("MEG")
  }

  test("3-word initials take precedent over skip-words") {
    TeamCodes.codeFor(new Team("a", "Real Hyper Test")) should equal("RHT")
  }

  test("Name with funny spaces doesn't break the 3-word initials") {
    TeamCodes.codeFor(new Team("a", "Bad  Name")) should equal("BAD")
  }
}
