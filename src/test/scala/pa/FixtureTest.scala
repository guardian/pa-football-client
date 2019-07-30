package pa

import org.scalatest.{FunSuite, Matchers, OptionValues}

import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class FixtureTest extends FunSuite with Matchers with OptionValues {

  val stubClient = StubClient
  val competitionId = "100"
  val matchFixtureOne = Await.result(stubClient.fixtures(competitionId), 10.seconds)(0)
  val matchFixtureTwo = Await.result(stubClient.fixtures(competitionId), 10.seconds)(1)

  test("Test parser returns two fixtures for test xml") {
    val fixtures = Await.result(stubClient.fixtures(competitionId), 10.seconds)
    fixtures.length should be (380)
  }

  test("Test formation of MatchDay fixture ids") {
    matchFixtureOne.id should be ("3924959")
    matchFixtureTwo.id should be ("3924960")
  }

  test("Test MatchDay fixture dates") {
    matchFixtureOne.date.getDayOfMonth should be (13)
    matchFixtureOne.date.getMonthValue should be (8)
    matchFixtureOne.date.getYear should be (2016)

    matchFixtureTwo.date.getDayOfMonth should be (13)
    matchFixtureTwo.date.getMonthValue should be (8)
    matchFixtureTwo.date.getYear should be (2016)
  }

  test("Test MatchDay fixture round") {
    matchFixtureOne.round.roundNumber should be ("1")
    matchFixtureOne.round.name.value should be ("League")

    matchFixtureTwo.round.roundNumber should be ("1")
    matchFixtureTwo.round.name.value should be ("League")
  }

  test("Test MatchDay fixture legs") {
    matchFixtureOne.leg should be ("1")
    matchFixtureTwo.leg should be ("1")
  }

  test("Test home and away teams within MatchDay fixture") {
    matchFixtureOne.homeTeam.id should be ("23")
    matchFixtureOne.homeTeam.name should be ("AFC Bournemouth")
    matchFixtureOne.awayTeam.id should be ("12")
    matchFixtureOne.awayTeam.name should be ("Man Utd")

    matchFixtureTwo.homeTeam.id should be ("1006")
    matchFixtureTwo.homeTeam.name should be ("Arsenal")
    matchFixtureTwo.awayTeam.id should be ("9")
    matchFixtureTwo.awayTeam.name should be ("Liverpool")
  }

  test("Test MatchDay fixture venue") {
    matchFixtureOne.venue.map(_.id).getOrElse("") should be ("1228")
    matchFixtureOne.venue.map(_.name).getOrElse("") should be ("Vitality Stadium")

    matchFixtureTwo.venue.map(_.id).getOrElse("") should be ("69")
    matchFixtureTwo.venue.map(_.name).getOrElse("") should be ("Emirates Stadium")
  }

  test("Test MatchDay fixture stage") {
    matchFixtureOne.stage.stageNumber should be ("1")
    matchFixtureTwo.stage.stageNumber should be ("1")
  }
  
  val fixtures = Await.result(stubClient.fixtures, 10.seconds)

  test("Test can get all Fixtures across all competitions") {
    fixtures(0).id should be ("3888456")
    fixtures(1).id should be ("3888457")
  }
  
  test("Test Fixture with a competition") {
    fixtures(0).competition.map(_.id).getOrElse("") should be ("750")
    fixtures(0).competition.map(_.name).getOrElse("") should be ("Euro 2016")
  }

  test("Test fixture throw exception if it encounters errors") {
    try {
      Await.result(stubClient.fixtures("errors"), 10.seconds)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }
  }
  
  
}
