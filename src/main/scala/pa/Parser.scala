package pa

import scala.Some
import xml.{NodeSeq, XML}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateMidnight

//There is always a certain amount of ugliness in parsing a feed.
//keep it all in one place
object Parser {

  private object EmptyToOption {
    def apply(s: String): Option[String] = s match {
      case null => None
      case "" => None
      case _ => Some(s)
    }
  }

  private object Date {

    val DateOnly = """(\d\d/\d\d/\d\d\d\d)""".r
    val DateWithTime = """(\d\d/\d\d/\d\d\d\d \d\d:\d\d)""".r

    private val DateParser = DateTimeFormat.forPattern("dd/MM/yyyy")
    private val DateTimeParser = DateTimeFormat.forPattern("ddd/MM/yyyy HH:mm")

    def apply(s: String): DateMidnight = s match {
      case DateOnly(date) => DateParser.parseDateTime(date)
      case DateWithTime(date) => DateTimeParser.parseDateTime(date)
    }
  }

  def parseCompetitions(s: String): Seq[Season] = (XML.loadString(s) \\ "season") map { season =>
    Season(
      season \@ "competitionID",
      season \> "name",
      Date(season \> "startDate"),
      Date(season \> "endDate")
    )
  }

  def parseMatchEvents(s: String): MatchEvents = {

    val xml = XML.loadString(s)

    def parseTeam(team: NodeSeq) = Team(
      team \@ "teamID",
      team.text
    )

    def parsePlayer(player: NodeSeq): Option[Player] = (player \@ "playerID") map {
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

    MatchEvents(
      parseTeam(xml \\ "homeTeam"),
      parseTeam(xml \\ "awayTeam"),
      (xml \ "events" \\ "event") map { parseEvent }
    )
  }

  def parseMatchStats(s: String): MatchStats = {
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

    MatchStats(
      matchStats \> "possession" toInt,
      parseTeam(matchStats \ "homeTeam"),
      parseTeam(matchStats \ "awayTeam")
    )
  }


  def parseMatchDay(s: String) = {

    def parseTeam(team: NodeSeq): MatchDayTeam = MatchDayTeam(
      team \@ "teamID",
      team \> "teamName",
      (team \> "score") map (_.toInt),
      (team \> "htScore") map (_.toInt),
      (team \> "aggregateScore") map (_.toInt),
      team \> "scorers"
    )

    def parseReferee(official: NodeSeq) = (official \@ "refereeID") flatMap { id =>
      if (official.text == "") None else Some(Official(id, official.text))
    }

    def parseVenue(venue: NodeSeq) = (venue \@ "venueID") map { id =>
      Venue(id, venue.text)
    }

    def parseRound(round: NodeSeq) = (round \@ "roundNumber") map { number =>
      Round(number, EmptyToOption(round.text))
    }

    XML.loadString(s) \ "match" map { aMatch =>
      MatchDay(
        aMatch \@ "matchID",
        Date((aMatch \@ "date") + " " + (aMatch \@ "koTime").getOrElse("")),
        parseRound(aMatch \ "round"),
        aMatch \> "leg",
        aMatch \> "liveMatch",
        aMatch \> "result",
        aMatch \> "previewAvailable",
        aMatch \> "reportAvailable",
        aMatch \> "lineupsAvailable",
        aMatch \> "matchStatus",
        aMatch \> "attendance",
        parseTeam(aMatch \ "homeTeam"),
        parseTeam(aMatch \ "awayTeam"),
        parseReferee(aMatch \ "referee"),
        parseVenue(aMatch \ "venue")
      )
    }
  }

  def parseLeagueTable(s: String): Seq[LeagueTableEntry] = {

    def parseRound(round: NodeSeq) = (round \@ "roundNumber") map { number =>
      Round(number, EmptyToOption(round.text))
    }

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
}