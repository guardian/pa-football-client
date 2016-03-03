package pa

import org.joda.time.{Interval, DateTime, LocalDate}

case class Error(message: String)

case class Season(competitionId: String, seasonId: String, name: String, startDate: LocalDate, endDate: LocalDate){
  lazy val interval: Interval = new Interval(startDate.toDateTimeAtStartOfDay, endDate.toDateTimeAtStartOfDay)

  // for backwards-compatibility
  val id = competitionId
}

case class Competition(id: String, name: String)

case class MatchEvents(homeTeam: Team, awayTeam: Team, events: List[MatchEvent], isResult: Boolean) {

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
  addedTime: Option[String],
  players: List[Player],
  reason: Option[String],
  how: Option[String],
  whereFrom: Option[String],
  whereTo: Option[String],
  distance: Option[String],
  outcome: Option[String]
) {

  val isGoal = outcome.exists(_ == "Goal")

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

case class LeagueTableEntry(stageNumber: String, round: Round, team: LeagueTeam)

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
  def stage: Stage
  def round: Round
  def leg: String
  def homeTeam: MatchDayTeam
  def awayTeam: MatchDayTeam
  def venue: Option[Venue]
  def comments: Option[String]
}

case class Fixture(
   id: String,
   date: DateTime,
   stage: Stage,
   round: Round,
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
  stage: Stage,
  round: Round,
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
  stage: Stage,
  round: Round,
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
  stage: Stage,
  round: Round,
  leg: String,
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

case class Head2Head(
  id: String,
  name: String,
  goals: Head2HeadStat,
  bookings: Head2HeadStat,
  dismissals: Head2HeadStat,
  substitutions: Head2HeadStat
) extends FootballTeam {
  def totalGoals = goals.awayCount + goals.homeCount
  def totalBookings = bookings.awayCount + bookings.homeCount
  def totalDismissals = dismissals.awayCount + dismissals.homeCount
  def totalSubstitutions = substitutions.awayCount + substitutions.homeCount
}
case class MatchInfo(id: String, matchDate: DateTime, description: String)
case class Head2HeadStat(homeCount: Int, homeMatches: List[MatchInfo], awayCount: Int, awayMatches: List[MatchInfo])

case class TeamEventMatch(
  id: String,
  date: DateTime,
  competitionId: String,
  stage: Int,
  round: Int,
  leg: Int,
  homeTeam: TeamEventMatchTeam,
  awayTeam: TeamEventMatchTeam,
  events: TeamEventMatchEvents
)
case class TeamEventMatchTeam(
  id: String,
  name: String,
  score: Int,
  htScore: Int,
  aggregateScore: Option[Int]
)
case class TeamEventMatchEvents(
  bookings: List[TeamEventMatchBooking],
  dismissals: List[TeamEventMatchDismissal],
  goals: List[TeamEventMatchGoal],
  penalties: List[TeamEventMatchPenalty],
  substitutions: List[TeamEventMatchSubstitution],
  shootoutPenalties: List[TeamEventMatchShootoutPenalty],
  other: List[TeamEventMatchOther]
)
trait TeamEventMatchEvent {
  val eventId: String
  val normalTime: String
  val addedTime: String
  val team: Team
}
case class TeamEventMatchBooking(
  eventId: String,
  normalTime: String,
  addedTime: String,
  team: Team,
  player: Player,
  reason: String
) extends TeamEventMatchEvent
case class TeamEventMatchDismissal(
  eventId: String,
  normalTime: String,
  addedTime: String,
  team: Team,
  player: Player,
  reason: String
) extends TeamEventMatchEvent
case class TeamEventMatchGoal(
  eventId: String,
  normalTime: String,
  addedTime: String,
  team: Team,
  player: Player,
  ownGoal: Boolean,
  how: Option[String],
  whereFrom: Option[String],
  whereTo: Option[String],
  distanceInYards: Option[String]
) extends TeamEventMatchEvent
case class TeamEventMatchPenalty(
  eventId: String,
  normalTime: String,
  addedTime: String,
  team: Team,
  player: Player,
  how: Option[String],
  whereTo: Option[String],
  outcome: String,
  keeperCorrect: Option[Boolean],
  `type`: Option[String]
) extends TeamEventMatchEvent
case class TeamEventMatchSubstitution(
  eventId: String,
  normalTime: String,
  addedTime: String,
  team: Team,
  playerOn: Player,
  playerOff: Player,
  how: Option[String],
  reason: Option[String]
) extends TeamEventMatchEvent
case class TeamEventMatchShootoutPenalty(
  eventId: String,
  team: Team,
  player: Player,
  how: Option[String],
  `type`: Option[String],
  whereTo: Option[String],
  keeperCorrect: Option[Boolean],
  outcome: Option[String]
)
case class TeamEventMatchOther(
  eventId: String,
  normalTime: String,
  addedTime: String,
  team: Team,
  player: Option[Player],
  eventType: String,
  how: Option[String],
  `type`: Option[String],
  whereFrom: Option[String],
  whereTo: Option[String],
  distanceInYards: Option[Int],
  outcome: Option[String],
  onTarget: Option[Boolean]
) extends TeamEventMatchEvent

private object Formats {
  val HoursMinutes = """^(\d+):(\d+)$""".r
}

case class SquadMember(
  playerId: String,
  name: String,
  squadNumber: Option[String],
  startDate: LocalDate,
  endDate: Option[LocalDate],
  onLoan: Boolean
)

case class PlayerAppearances(
  playerName: String,
  home: Appearances,
  away: Appearances,
  total: Appearances
)
case class Appearances(
  appearances: Int,
  started: Int,
  substitutedOn: Int,
  substitutedOff: Int,
  dismissals: Int
)

case class StatsSummary(
  defence: PlayerStatsSummaryDefence,
  offence: PlayerStatsSummaryOffense,
  discipline: PlayerStatsSummaryDiscipline,
  substitutionsOn: Stat,
  substitutionsOff: Stat,
  totalGoalsAgainst: Stat,
  totalGoalsFor: Stat
)
case class PlayerStatsSummaryOffense(
  assists: Stat,
  corners: Stat,
  crosses: Stat,
  freeKicks: Stat,
  goals: Stat,
  penalties: Stat,
  shotsOffTarget: Stat,
  shotsOnTarget: Stat,
  throwIns: Stat
) {
  private def percentage(n: Float, m: Float): Int = {
    if (0 == n) 0
    else if (0 == n + m) 100
    else Math.round((n / (n + m)) * 100)
  }

  val shotsOnTargetPercentage = new Stat(
    percentage(shotsOnTarget.home, shotsOffTarget.home),
    percentage(shotsOnTarget.away, shotsOffTarget.away),
    "Shots On Target Percentage",
    "0"
  ) {
    override val total = percentage(shotsOnTarget.total, shotsOffTarget.total)
  }
}
case class PlayerStatsSummaryDefence(
  backPasses: Stat,
  blocks: Stat,
  clearances: Stat,
  goalKicks: Stat,
  goalsAgainst: Stat,
  ownGoals: Stat,
  ownGoalsFor: Stat,
  saves: Stat
)
case class PlayerStatsSummaryDiscipline(
  bookings: Stat,
  dismissals: Stat,
  foulsAgainst: Stat,
  foulsCommitted: Stat,
  handBalls: Stat,
  offsides: Stat,
  tenYards: Stat
)
case class Stat(home: Int, away: Int, statDescription: String, statTypeId: String) {
  val total = home + away
}

case class PlayerProfile(
  fullName: String,
  height: Option[String],
  weight: Option[String],
  dob: Option[LocalDate],
  age: Option[String],
  nationality: Option[String],
  position: Option[String]
)
