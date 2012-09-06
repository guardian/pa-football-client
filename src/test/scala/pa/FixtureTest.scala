package pa

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.matchers.ShouldMatchers

class ParserTest extends FunSuite with ShouldMatchers {

  val stubClient = StubClient
  val matchFixtureOne = stubClient.fixtures("789")(0)
  val matchFixtureTwo = stubClient.fixtures("789")(1)

  test("Test parser returns two fixtures for test xml") {
    val fixtures = stubClient.fixtures("789")
    fixtures.length should be (2)
  }

  test("Test formation of MatchDay fixture ids") {
    matchFixtureOne.id should be ("3407177")
    matchFixtureTwo.id should be ("3407178")
  }

  test("Test MatchDay fixture dates") {
    matchFixtureOne.date.dayOfMonth.get should be (13)
    matchFixtureOne.date.monthOfYear.get should be (8)
    matchFixtureOne.date.year.get should be (2011)

    matchFixtureTwo.date.dayOfMonth.get should be (13)
    matchFixtureTwo.date.monthOfYear.get should be (8)
    matchFixtureTwo.date.year.get should be (2011)
  }

  test("Test MatchDay fixture round") {
    matchFixtureOne.round.map(_.roundNumber).getOrElse("") should be ("1")
    matchFixtureOne.round.flatMap(_.name).getOrElse("nothing") should be ("nothing")

    matchFixtureTwo.round.map(_.roundNumber).getOrElse("") should be ("7")
    matchFixtureTwo.round.flatMap(_.name).getOrElse("") should be ("round")
  }

  test("Test MatchDay fixture legs") {
    matchFixtureOne.leg should be ("1")
    matchFixtureTwo.leg should be ("7")
  }

  test("Test home and away teams within MatchDay fixture") {
    matchFixtureOne.homeTeam.id should be ("22")
    matchFixtureOne.homeTeam.name should be ("Blackburn")
    matchFixtureOne.awayTeam.id should be ("44")
    matchFixtureOne.awayTeam.name should be ("Wolverhampton")

    matchFixtureTwo.homeTeam.id should be ("55")
    matchFixtureTwo.homeTeam.name should be ("Fulham")
    matchFixtureTwo.awayTeam.id should be ("2")
    matchFixtureTwo.awayTeam.name should be ("Aston Villa")
  }

  test("Test MatchDay fixture venue") {
    matchFixtureOne.venue.map(_.id).getOrElse("") should be ("59")
    matchFixtureOne.venue.map(_.name).getOrElse("") should be ("Ewood Park")

    matchFixtureTwo.venue.map(_.id).getOrElse("") should be ("60")
    matchFixtureTwo.venue.map(_.name).getOrElse("") should be ("Craven Cottage")
  }

  test("Test MatchDay fixture unused values") {
    matchFixtureOne.liveMatch should be (false)
    matchFixtureOne.result should be (false)
    matchFixtureOne.previewAvailable should be (false)
    matchFixtureOne.reportAvailable should be (false)
    matchFixtureOne.lineupsAvailable should be (false)
    matchFixtureOne.attendance should be (None)
    matchFixtureOne.referee should be (None)
  }
}
