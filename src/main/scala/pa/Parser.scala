package pa

import scala.Some
import xml.{NodeSeq, XML}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

//There is always a certain amount of ugliness in parsing a feed.
//keep it all in one place
object Parser {

  private object Date {

    private val DateParser = DateTimeFormat.forPattern("dd/MM/yyyy")
    private val DateTimeParser = DateTimeFormat.forPattern("ddd/MM/yyyy HH:mm")

    def apply(date: String): DateTime = DateParser.parseDateTime(date)

    def apply(date: String, time: String) = DateTimeParser.parseDateTime("%s %s".format(date, time))
  }

  def parseCompetitions(s: String): List[Season] = (XML.loadString(s) \\ "season") map { season =>
    Season(
      season \@ "competitionID",
      season \> "name",
      Date(season \> "startDate"),
      Date(season \> "endDate")
    )
  }

  def parseMatchEvents(s: String): Option[MatchEvents] = {

    val xml = XML.loadString(s)

    def parseTeam(team: NodeSeq) = Team(
      team \@ "teamID",
      team.text
    )

    def parsePlayer(player: NodeSeq): Option[Player] = (player \@@ "playerID") map {
        Player(_, player \@ "teamID", player.text)
    }

    def parseEvent(event: NodeSeq) = Event(
      event \@ "eventID",
      event \@ "teamID",
      event \> "eventType",
      event \> "matchTime",
      event \> "eventTime",
      ((event \\ "player1") ++ (event \\ "player2")) flatMap { parsePlayer },
      event \> "reason",
      event \> "how",
      event \> "whereFrom",
      event \> "whereTo",
      event \> "distance",
      event \> "outcome"
    )

    //annoyingly there are some matches with no events
    //marked up in a weird way
    xml \ "teams" \>> "homeTeam" map { homeTeam =>
      MatchEvents(
        parseTeam(xml \\ "homeTeam"),
        parseTeam(xml \\ "awayTeam"),
        (xml \ "events" \\ "event") map { parseEvent }
      )
    }
  }

  def parseMatchStats(s: String): Option[MatchStats] = {
    val matchStats = XML.loadString(s)

    def parseTeam(team: NodeSeq) = TeamStats(
      team \> "bookings" toInt,
      team \> "dismissals" toInt,
      team \> "corners" toInt,
      team \> "offsides" toInt,
      team \> "fouls" toInt,
      team \> "shotsOnTarget" toInt,
      team \> "shotsOffTarget" toInt
    )

    if ((matchStats \ "errors").nonEmpty )
      None
    else
      Some(
        MatchStats(
          matchStats \> "possession" toInt,
          parseTeam(matchStats \ "homeTeam"),
          parseTeam(matchStats \ "awayTeam")
        )
      )
  }

  def parseMatchDay(s: String) = {

    def parseTeam(team: NodeSeq): MatchDayTeam = MatchDayTeam(
      team \@ "teamID",
      team \> "teamName",
      (team \>> "score") map (_.toInt),
      (team \>> "htScore") map (_.toInt),
      (team \>> "aggregateScore") map (_.toInt),
      team \> "scorers"
    )

    XML.loadString(s) \ "match" map { aMatch =>
      MatchDay(
        aMatch \@ "matchID",
        Date(aMatch \@ "date", aMatch \@ "koTime"),
        parseRound(aMatch \ "round"),
        aMatch \> "leg",
        aMatch \>> "liveMatch",
        aMatch \>> "result",
        aMatch \>> "previewAvailable",
        aMatch \>> "reportAvailable",
        aMatch \>> "lineupsAvailable",
        aMatch \> "matchStatus",
        aMatch \> "attendance",
        parseTeam(aMatch \ "homeTeam"),
        parseTeam(aMatch \ "awayTeam"),
        parseReferee(aMatch \ "referee"),
        parseVenue(aMatch \ "venue")
      )
    }
  }

  def parseResults(s: String): List[Result] = {

    def parseTeam(team: NodeSeq): MatchDayTeam = MatchDayTeam(
      team \@ "teamID",
      team \> "name",
      (team \>> "score") map (_.toInt),
      (team \>> "htScore") map (_.toInt),
      (team \>> "aggregateScore") map (_.toInt),
      team \> "scorers"
    )

    XML.loadString(s) \ "result" map { result =>
      Result(
        result \@ "matchID",
        Date(result \@ "date", result \@ "koTime"),
        parseRound(result \ "round"),
        result \> "leg",
        result \>> "reportAvailable",
        result \> "attendance",
        parseTeam(result \ "homeTeam"),
        parseTeam(result \ "awayTeam"),
        parseReferee(result \ "referee"),
        parseVenue(result \ "venue"),
        parseCompetition(result \ "competition")
      )
    }
  }

  def parseLeagueTable(s: String): List[LeagueTableEntry] = {

    (XML.loadString(s) \ "tableEntry") map { entry =>

      val team = entry \ "team"

      LeagueTableEntry(
        entry \> "stageNumber",
        parseRound(entry \ "round"),
        LeagueTeam(
          team \@ "teamID",
          team \@ "teamName",
          team \> "rank" toInt,
          team \> "played" toInt,
          team \> "won" toInt,
          team \> "drawn" toInt,
          team \> "lost" toInt,
          team \> "for" toInt,
          team \> "against" toInt,
          team \> "goalDifference" toInt,
          team \> "points" toInt
        )
      )
    }
  }

  def parseFixtures(s: String): List[Fixture] = {

    def parseTeam(team: NodeSeq): MatchDayTeam = MatchDayTeam(
      team \@ "teamID",
      team.text,
      score          = None,
      htScore        = None,
      aggregateScore = None,
      scorers        = None
    )

    def parseStage(stage: NodeSeq): Stage = Stage (
      stageNumber = stage \@ "stageNumber"
    )

    (XML.loadString(s) \\ "fixtures" \ "fixture") map { fixture =>
      Fixture(
        fixtureId                = fixture \@ "matchID",
        fixtureDate              = Date(fixture \@ "date", fixture \@ "koTime"),
        stage                    = parseStage(fixture \ "stage"),
        fixtureRound             = parseRound(fixture \ "round"),
        fixtureLeg               = fixture \> "leg",
        fixtureHomeTeam          = parseTeam(fixture \ "homeTeam"),
        fixtureAwayTeam          = parseTeam(fixture \ "awayTeam"),
        fixtureVenue             = parseVenue(fixture \ "venue"),
        competition				 = parseCompetition(fixture \ "competition")
      )
    }
  }
  

  protected def parseReferee(official: NodeSeq) = (official \@@ "refereeID") flatMap { id =>
    if (official.text == "") None else Some(Official(id, official.text))
  }

  protected def parseCompetition(competition: NodeSeq) = (competition \@@ "competitionID") map { id =>
  	Competition(id, competition.text)
  }
  
  protected def parseRound(round: NodeSeq) = (round \@@ "roundNumber") map { number =>
    Round(number, round.text)
  }

  protected def parseVenue(venue: NodeSeq) = (venue \@@ "venueID") map { id =>
    Venue(id, venue.text)
  }

}