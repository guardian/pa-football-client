package pa

import org.joda.time.{Interval, DateTime, DateMidnight}

case class Season(id: String, name: String, startDate: DateMidnight, endDate: DateMidnight){
  lazy val interval: Interval = new Interval(startDate, endDate)
}

case class Competition(id: String, name: String)

case class MatchEvents(homeTeam: Team, awayTeam: Team, events: List[MatchEvent]) {

  val goals = events.filter(_.isGoal)

  val homeTeamGoals = goals.filter(_.teamID == Some(homeTeam.id))

  val awayTeamGoals = goals.filter(_.teamID == Some(awayTeam.id))

  val homeTeamScore = homeTeamGoals.size

  val awayTeamScore = awayTeamGoals.size
}

trait Person {
  val id: String
  val name: String
}

case class Player(id: String, teamID: String, name: String) extends Person

case class MatchEvent(
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
  outcome: Option[String],
  gameEventType: Option[String]
) {

  val isGoal = outcome map (_ == "Goal") getOrElse false

}

case class MatchStats(interval: Int, homePossession: Int, homeTeam: TeamStats, awayTeam: TeamStats) {
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

case class Official(id: String, name: String) extends Person
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

case class LeagueStats(
  played: Int,
  won: Int,
  drawn: Int,
  lost: Int,
  goalsFor: Int,
  goalsAgainst: Int
)

case class LeagueTeam(
  id: String,
  name: String,
  rank: Int,
  total: LeagueStats,
  home: LeagueStats,
  away: LeagueStats,
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
  def comments: Option[String]
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
   competition: Option[Competition]) extends FootballMatch {
  override val comments = None
}

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
  venue: Option[Venue],
  comments: Option[String]
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
  venue: Option[Venue],
  comments: Option[String]
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

case class LineUpEvent(
  eventType: String,
  eventTime: String,
  matchTime: String
)

case class LineUpTeam (
  id: String,
  name: String,
  teamColour: String,
  manager: Official,
  formation: String,
  shotsOn: Int,
  shotsOff: Int,
  fouls: Int,
  corners: Int,
  offsides: Int,
  bookings: Int,
  dismissals: Int,
  players: Seq[LineUpPlayer]
) extends FootballTeam

case class LineUpPlayer(
  id: String,
  name: String,
  firstName: String,
  lastName: String,
  shirtNumber: String,
  position: String,
  substitute: Boolean,
  rating: Option[Int],
  timeOnPitch: String,
  events: Seq[LineUpEvent]
) extends Person

case class LineUp(homeTeam: LineUpTeam, awayTeam: LineUpTeam, homeTeamPossession: Int) {
  lazy val awayTeamPossession = 100 - homeTeamPossession
}

case class EAIndexTeam(
  id: String,
  name: String
) extends FootballTeam

case class EAIndexTeamMembership(
  team: EAIndexTeam,
  startDate: DateTime,
  onLoan: Boolean,
  squadNumber: Option[Int]
)

case class EAIndexPlayerMatchStatistics(
  matchID: String,
  date: DateTime,
  index: Int,
  minutesOnPitch: Int,
  allGoals: Int,
  ownGoals: Int,
  dismissals: Int,
  bookings: Int,
  shotsOnTarget: Int,
  shotsOffTarget: Int,
  fouls: Int,
  tacklesWon: Int,
  tacklesLost: Int,
  clearances: Int,
  interceptions: Int,
  saves: Int,
  blocks: Int,
  passes: Int,
  dribbles: Int,
  crosses: Int,
  lastUpdated: DateTime
)

case class EAIndexPlayer(
  id: String,
  name: String,
  height: String,
  weight: String,
  dateOfBirth: DateTime,
  age: Int,
  nationality: String,
  teams: List[EAIndexTeamMembership],
  position: String,
  matches: List[EAIndexPlayerMatchStatistics]
) extends Person

private object Formats {
  val HoursMinutes = """^(\d+):(\d+)$""".r
}
