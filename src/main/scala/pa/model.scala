package pa

import org.joda.time.{DateTime, DateMidnight}

case class Season(competitionID: String, name: String, startDate: DateMidnight, endDate: DateMidnight)

case class MatchEvents(homeTeam: Team, awayTeam: Team, events: Seq[Event]) {

  val goals = events.filter(_.isGoal)

  val homeTeamGoals = goals.filter(_.teamID == homeTeam.teamID)

  val awayTeamGoals = goals.filter(_.teamID == awayTeam.teamID)

  val homeTeamScore = homeTeamGoals.size

  val awayTeamScore = awayTeamGoals.size
}

case class Team(teamID: String, name: String)

case class Player(playerID: String, teamID: String, name: String)

//TODO some of these are nullable and need converions to Option
case class Event(
  teamID: String,
  eventID: String,
  eventType: String,
  matchTime: String,
  eventTime: String,
  players: Seq[Player],
  reason: Option[String],
  how: String,
  whereFrom: String,
  whereTo: String,
  distance: String,
  outcome: Option[String]
) {

  val isGoal = outcome.map(_ == "Goal").getOrElse(false)

}

case class MatchStats(homePossession: Int, homeTeam: TeamStats, awayTeam: TeamStats) {
  lazy val awayPossession = 100 - homePossession
}

case class TeamStats(
  bookings: Int,
  dismissals: Int,
  corners: Int,
  offsides: Int,
  fouls: Int,
  shotsOnTarget: Int,
  shotsOffTarget: Int
)

case class Official(id: String, name: String)
case class Venue(venueID: String, name: String)
case class Round(roundNumber: String, name: String)

case class MatchDayTeam(
  teamID: String,
  teamName: String,
  score: Option[Int],
  htScore: Option[Int],
  aggregateScore: Option[Int],
  scorers: Option[String]
)

case class MatchDay(
  matchID: String,
  date: DateMidnight,
  koTime: String,
  round: Option[Round],
  leg: String,
  liveMatch: Boolean,
  result: Boolean,
  previewAvailable: Boolean,
  reportAvailable: Boolean,
  lineupsAvailable: Boolean,
  matchStatus: String,
  attendance: String,
  homeTeam: MatchDayTeam,
  awayTeam: MatchDayTeam,
  referee: Option[Official],
  venue: Option[Venue]
) {
  import Formats._
  lazy val kickOffTime: DateTime = koTime match {
    case HoursMinutes(hours, minutes) => new DateTime(date).plusHours(hours.toInt).plusMinutes(minutes.toInt)
  }
}

private object Formats {
  val HoursMinutes = """^(\d*):(\d*)$""".r
}