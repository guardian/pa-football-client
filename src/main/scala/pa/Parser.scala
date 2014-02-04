package pa

import scala.Some
import xml.{NodeSeq, XML}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import language.reflectiveCalls
import language.postfixOps

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
      competitionId = season \@ "competitionID",
      seasonId      = season \@ "seasonID",
      name          = season \> "name",
      startDate     = Date(season \> "startDate"),
      endDate       = Date(season \> "endDate")
    )
  }

  def parseLineUp(s: String): LineUp = {
    val xml = XML.loadString(s)

    def parsePlayer(player: NodeSeq) = LineUpPlayer(
      player \@ "playerID",
      player \> "fullName",
      player \> "firstName",
      player \> "lastName",
      player \> "shirtNumber",
      player \> "position",
      player \> "substitute",
      (player \>> "rating") map (_.toInt) ,
      player \> "timeOnPitch",
      (player \\ "event") map parseEvent
    )

    def parseEvent(event: NodeSeq) = LineUpEvent(
      event \@ "type",
      event \@ "eventTime",
      event \@ "matchTime"
    )

    def parseTeam(team: NodeSeq): LineUpTeam = LineUpTeam(
      team \@ "teamID",
      team \@ "teamName",
      team \@ "teamColour",
      Official(
        team \@ "managerID",
        team \@ "manager"
      ),
      team \@ "formation",
      team \@ "shotsOn" toInt,
      team \@ "shotsOff" toInt,
      team \@ "fouls" toInt,
      team \@ "corners" toInt,
      team \@ "offsides" toInt,
      team \@ "bookings" toInt,
      team \@ "dismissals" toInt,
      (team \\ "player") map parsePlayer
    )

    LineUp(
      parseTeam(xml \ "teams" \ "homeTeam"),
      parseTeam(xml \ "teams" \ "awayTeam"),
      xml \@ "possession" toInt
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

    def parseEvent(event: NodeSeq) = MatchEvent(
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

  def parseMatchStats(s: String): Seq[MatchStats] = {

    def parseTeam(team: NodeSeq) = {
      TeamStats(
        team \ "bookings" \@ "total" toInt,
        team \ "dismissals" \@ "total" toInt,
        team \ "corners" \@ "total" toInt,
        team \ "offsides" \@ "total" toInt,
        team \ "fouls" \@ "total" toInt,
        team \ "shotsOnTarget" \@ "total" toInt,
        team \ "shotsOffTarget" \@ "total" toInt
      )
    }

    (XML.loadString(s) \\ "stat").map{ stats =>
      MatchStats(
        stats \@ "interval" toInt,
        stats \> "possession" toInt,
        parseTeam(stats \ "homeTeam"),
        parseTeam(stats \ "awayTeam")
      )
    }
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
        parseCompetition(aMatch \ "competition"),
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
        parseVenue(aMatch \ "venue"),
        aMatch \>> "comments"
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
        result \>> "comments"
      )
    }
  }

  def parseLiveMatches(s: String): List[LiveMatch] = {

    def parseTeam(team: NodeSeq): MatchDayTeam = MatchDayTeam(
      team \@ "teamID",
      team \> "name",
      (team \>> "score") map (_.toInt),
      (team \>> "htScore") map (_.toInt),
      (team \>> "aggregateScore") map (_.toInt),
      team \> "scorers"
    )

    XML.loadString(s) \ "match" map { aMatch =>
      LiveMatch(
        aMatch \@ "matchID",
        Date(aMatch \@ "date", aMatch \@ "koTime"),
        parseRound(aMatch \ "round"),
        aMatch \> "attendance",
        parseTeam(aMatch \ "homeTeam"),
        parseTeam(aMatch \ "awayTeam"),
        parseReferee(aMatch \ "referee"),
        parseVenue(aMatch \ "venue"),
        aMatch \> "matchStatus",
        aMatch \> "comments"
      )
    }
  }

  def parseLeagueTable(s: String): List[LeagueTableEntry] = {

    def parseLeagueStats(stats: NodeSeq) = LeagueStats(
      stats \> "played" toInt,
      stats \> "won" toInt,
      stats \> "drawn" toInt,
      stats \> "lost" toInt,
      stats \> "for" toInt,
      stats \> "against" toInt
    )

    (XML.loadString(s) \ "tableEntry") map { entry =>

      val team = entry \ "team"

      LeagueTableEntry(
        entry \> "stageNumber",
        parseRound(entry \ "round"),
        LeagueTeam(
          team \@ "teamID",
          team \@ "teamName",
          team \> "rank" toInt,
          parseLeagueStats(team),
          parseLeagueStats(team \ "home"),
          parseLeagueStats(team \ "away"),
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
        id                = fixture \@ "matchID",
        date              = Date(fixture \@ "date", fixture \@ "koTime"),
        stage                    = parseStage(fixture \ "stage"),
        round             = parseRound(fixture \ "round"),
        leg               = fixture \> "leg",
        homeTeam          = parseTeam(fixture \ "homeTeam"),
        awayTeam          = parseTeam(fixture \ "awayTeam"),
        venue             = parseVenue(fixture \ "venue"),
        competition				 = parseCompetition(fixture \ "competition")
      )
    }
  }

  def parseTeamHead2Head(s: String): (TeamHead2Head, TeamHead2Head) = {
    def parseHead2HeadStat(stat: NodeSeq): Head2HeadStat = {
      val home = stat \ "home"
      val away = stat \ "away"
      Head2HeadStat(
        homeCount   = (home \@ "total").toInt,
        homeMatches = (home \ "matches" \ "match") map { m => MatchInfo(m \@ "matchID", Date(m \@ "date", m \@ "koTime"), m \@ "description") },
        awayCount   = (away \@ "total").toInt,
        awayMatches = (away \ "matches" \ "match") map { m => MatchInfo(m \@ "matchID", Date(m \@ "date", m \@ "koTime"), m \@ "description") }
      )
    }
    def parseTeamHead2Head(team: NodeSeq): TeamHead2Head = {
      TeamHead2Head(
        id            = team \@ "teamID",
        name          = team \@ "name",
        goals         = parseHead2HeadStat(team \ "goals"),
        bookings      = parseHead2HeadStat(team \ "bookings"),
        dismissals    = parseHead2HeadStat(team \ "dismissals"),
        substitutions = parseHead2HeadStat(team \ "substitutions")
      )
    }

    val teams = XML.loadString(s) \\ "headToHeads" \ "teams"
    (
      parseTeamHead2Head(teams \ "teamOne"),
      parseTeamHead2Head(teams \ "teamTwo")
    )
  }

  def parseTeamEventMatches(s: String): List[TeamEventMatch] = {
    def parseMatchTeam(team: NodeSeq): TeamEventMatchTeam = {
      TeamEventMatchTeam(
        id             = team \@ "teamID",
        name           = team \> "name",
        score          = (team \> "score").toInt,
        htScore        = (team \> "htScore").toInt,
        aggregateScore = (team \>> "aggregateScore").map(_.toInt)
      )
    }
    def parseTeamEventMatchEvents(events: NodeSeq): TeamEventMatchEvents = {
      TeamEventMatchEvents(
        bookings = (events \ "bookings" \ "booking") map { node =>
          TeamEventMatchBooking(
            eventId = node \@ "eventID",
            normalTime = node \> "normalTime",
            addedTime = node \> "addedTime",
            team = Team(node \ "team" \@ "teamID", node \> "team"),
            player = {
              val playerNode = node \ "player"
              Player(playerNode \@ "playerID", node \ "team" \@ "teamID", playerNode.text)
            },
            reason = node \> "reason"
          )
        },
        dismissals = (events \ "dismissals" \ "dismissal") map { node =>
          TeamEventMatchDismissal(
            eventId = node \@ "eventID",
            normalTime = node \> "normalTime",
            addedTime = node \> "addedTime",
            team = Team(node \ "team" \@ "teamID", node \> "team"),
            player = {
              val playerNode = node \ "player"
              Player(playerNode \@ "playerID", node \ "team" \@ "teamID", playerNode.text)
            },
            reason = node \> "reason"
          )
        },
        goals = (events \ "goals" \ "goal") map { node =>
          TeamEventMatchGoal(
            eventId = node \@ "eventID",
            normalTime = node \> "normalTime",
            addedTime = node \> "addedTime",
            team = Team(node \ "team" \@ "teamID", node \> "team"),
            player = {
              val playerNode = node \ "player"
              Player(playerNode \@ "playerID", node \ "team" \@ "teamID", playerNode.text)
            },
            ownGoal = (node \> "ownGoal") == "Yes",
            how = node \>> "how",
            whereFrom = node \>> "whereFrom",
            whereTo = node \>> "whereTo",
            distanceInYards = node \>> "distanceInYards"
          )
        },
        penalties = (events \ "penalties" \ "penalty") map { node =>
          TeamEventMatchPenalty(
            eventId = node \@ "eventID",
            normalTime = node \> "normalTime",
            addedTime = node \> "addedTime",
            team = Team(node \ "team" \@ "teamID", node \> "team"),
            player = {
              val playerNode = node \ "player"
              Player(playerNode \@ "playerID", node \ "team" \@ "teamID", playerNode.text)
            },
            how = node \>> "how",
            whereTo = node \>> "whereTo",
            outcome = node \> "outcome",
            keeperCorrect = (node \>> "keeperCorrect") map ("Yes"==),
            `type` = node \>> "type"
          )
        },
        substitutions = (events \ "substitutions" \ "substitution") map { node =>
          TeamEventMatchSubstitution(
            eventId = node \@ "eventID",
            normalTime = node \> "normalTime",
            addedTime = node \> "addedTime",
            team = Team(node \ "team" \@ "teamID", node \> "team"),
            playerOn = {
              val playerNode = node \ "playerOn"
              Player(playerNode \@ "playerID", node \ "team" \@ "teamID", playerNode.text)
            },
            playerOff = {
              val playerNode = node \ "playerOff"
              Player(playerNode \@ "playerID", node \ "team" \@ "teamID", playerNode.text)
            },
            how = node \>> "how",
            reason = node \>> "reason"
          )
        },
        shootoutPenalties = (events \ "shootOutPenalties" \ "shootOutPenalty") map { node =>
          TeamEventMatchShootoutPenalty(
            eventId = node \@ "eventID",
            team = Team(node \ "team" \@ "teamID", node \> "team"),
            player = {
              val playerNode = node \ "player"
              Player(playerNode \@ "playerID", node \ "team" \@ "teamID", playerNode.text)
            },
            how = node \>> "how",
            `type` = node \>> "type",
            whereTo = node \>> "whereTo",
            keeperCorrect = (node \>> "keeperCorrect") map ("Yes"==),
            outcome = node \>> "outcome"
          )
        },
        other = (events \ "other" \ "event") map { node =>
          TeamEventMatchOther(
            eventId = node \@ "eventID",
            normalTime = node \> "normalTime",
            addedTime = node \> "addedTime",
            team = Team(node \ "team1" \@ "teamID", node \> "team1"),
            player = (node \>> "player1").map { playerName =>
              Player(node \ "player1" \@ "playerID", node \ "team1" \@ "teamID", playerName)
            },
            eventType = node \> "eventType",
            how = node \>> "how",
            `type` = node \>> "type",
            whereFrom = node \>> "whereFrom",
            whereTo = node \>> "whereTo",
            distanceInYards = (node \>> "distanceInYards").map(_.toInt),
            outcome = node \>> "outcome",
            onTarget = (node \>> "onTarget") map ("Yes"==)
          )
        }
      )
    }

    (XML.loadString(s) \\ "matches" \ "match") map { matchNode =>
      val homeTeam = parseMatchTeam(matchNode \ "homeTeam")
      val awayTeam = parseMatchTeam(matchNode \ "awayTeam")
      TeamEventMatch(
        id            = matchNode \@ "matchID",
        date          = Date(matchNode \@ "date", matchNode \@ "koTime"),
        competitionId = matchNode \ "competition" \@ "competitionID",
        stage         = (matchNode \ "stage" \@ "stageNumber").toInt,
        round         = (matchNode \ "round" \@ "roundNumber").toInt,
        leg           = (matchNode \> "leg").toInt,
        homeTeam      = homeTeam,
        awayTeam      = awayTeam,
        events        = parseTeamEventMatchEvents(matchNode \ "events")
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