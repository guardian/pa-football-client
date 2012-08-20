package pa

import org.joda.time.DateMidnight

case class Season(competitionID: String, name: String, startDate: DateMidnight, endDate: DateMidnight)

case class Match(homeTeam: Team, awayTeam: Team, events: Seq[Event]) {

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

case class MatchStats(homeTeam: TeamStats, awayTeam: TeamStats)

case class TeamStats(
  bookings: Int,
  dismissals: Int,
  corners: Int,
  offsides: Int,
  fouls: Int,
  shotsOnTarget: Int,
  shotsOffTarget: Int
)