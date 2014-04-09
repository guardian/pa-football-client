package pa

import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class ParserTest extends FunSuite with ShouldMatchers {

  val stubClient = StubClient
  val matchFixtureOne = Await.result(stubClient.fixtures("789"), 1.second)(0)
  val matchFixtureTwo = Await.result(stubClient.fixtures("789"), 1.second)(1)

  test("Test parser returns two fixtures for test xml") {
    val fixtures = Await.result(stubClient.fixtures("789"), 1.second)
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

  test("Test MatchDay fixture stage") {
    matchFixtureOne.stage.stageNumber should be ("1")
    matchFixtureTwo.stage.stageNumber should be ("1")
  }
  
  val fixtures = Await.result(stubClient.fixtures, 1.second)

  test("Test can get all Fixtures across all competitions") {
    fixtures(0).id should be ("3407177")
    fixtures(1).id should be ("3407178")
  }
  
  test("Test Fixture with a competition") {
    fixtures(0).competition.map(_.id).getOrElse("") should be ("100")
    fixtures(0).competition.map(_.name).getOrElse("") should be ("Barclays Premier League 11/12")
  }
  
  test("Test Fixture without a competition") {
    fixtures(1).competition should be (None)
  }
  
  
}
