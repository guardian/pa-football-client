package pa

import org.joda.time.{Interval, DateTime, DateMidnight}

case class Season(id: String, name: String, startDate: DateMidnight, endDate: DateMidnight){
  lazy val interval: Interval = new Interval(startDate, endDate)
}

case class Competition(id: String, name: String)

case class MatchEvents(homeTeam: Team, awayTeam: Team, events: List[Event]) {

  val goals = events.filter(_.isGoal)

  val homeTeamGoals = goals.filter(_.teamID == Some(homeTeam.id))

  val awayTeamGoals = goals.filter(_.teamID == Some(awayTeam.id))

  val homeTeamScore = homeTeamGoals.size

  val awayTeamScore = awayTeamGoals.size
}

case class Player(id: String, teamID: String, name: String)

case class Event(
  id: Option[String],
  teamID: Option[String],
  eventType: String,
  matchTime: Option[String],
  eventTime: Option[String],
  players: List[Player],
  reason: Option[String],
  how: Option[String],
  whereFrom: Option[String],
  whereTo: Option[String],
  distance: Option[String],
  outcome: Option[String]
) {

  val isGoal = outcome map (_ == "Goal") getOrElse false

}

case class MatchStats(homePossession: Int, homeTeam: TeamStats, awayTeam: TeamStats) {
  lazy val awayPossession: Int = 100 - homePossession
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
case class Venue(id: String, name: String)
case class Round(roundNumber: String, name: Option[String])

case class LeagueTableEntry(stageNumber: String, round: Option[Round], team: LeagueTeam)

trait FootballTeam {
  def id: String
  def name: String
}

case class Team(id: String, name: String) extends FootballTeam

case class MatchDayTeam(
  id: String,
  name: String,
  score: Option[Int],
  htScore: Option[Int],
  aggregateScore: Option[Int],
  scorers: Option[String]
) extends FootballTeam

case class LeagueTeam(
  id: String,
  name: String,
  rank: Int,
  played: Int,
  won: Int,
  drawn: Int,
  lost: Int,
  goalsFor: Int,
  goalsAgainst: Int,
  goalDifference: Int,
  points: Int
) extends FootballTeam

trait FootballMatch {
  def id: String
  def date: DateTime
  def round: Option[Round]
  def homeTeam: MatchDayTeam
  def awayTeam: MatchDayTeam
  def venue: Option[Venue]
}

case class Fixture(
   id: String,
   date: DateTime,
   stage: Stage,
   round: Option[Round],
   leg: String,
   homeTeam: MatchDayTeam,
   awayTeam: MatchDayTeam,
   venue: Option[Venue],
   competition: Option[Competition]) extends FootballMatch

case class MatchDay(
  id: String,
  date: DateTime,
  competition: Option[Competition],
  round: Option[Round],
  leg: String,
  liveMatch: Boolean,
  result: Boolean,
  previewAvailable: Boolean,
  reportAvailable: Boolean,
  lineupsAvailable: Boolean,
  matchStatus: String,
  attendance: Option[String],
  homeTeam: MatchDayTeam,
  awayTeam: MatchDayTeam,
  referee: Option[Official],
  venue: Option[Venue]
) extends FootballMatch

case class Result(
  id: String,
  date: DateTime,
  round: Option[Round],
  leg: String,
  reportAvailable: Boolean,
  attendance: Option[String],
  homeTeam: MatchDayTeam,
  awayTeam: MatchDayTeam,
  referee: Option[Official],
  venue: Option[Venue]
) extends FootballMatch

case class LiveMatch(
  id: String,
  date: DateTime,
  round: Option[Round],
  attendance: Option[String],
  homeTeam: MatchDayTeam,
  awayTeam: MatchDayTeam,
  referee: Option[Official],
  venue: Option[Venue],
  status: String,
  comments: Option[String]
) extends FootballMatch

case class Stage(
    stageNumber: String
)

private object Formats {
  val HoursMinutes = """^(\d+):(\d+)$""".r
}
