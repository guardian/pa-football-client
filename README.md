A simple scala client for the PA football API.

[ ![Download](https://api.bintray.com/packages/guardian/frontend/pa-client/images/download.svg) ](https://bintray.com/guardian/frontend/pa-client/_latestVersion) 

*NOTE*: as of 4.0, Scala 2.9.x is no longer as the client has moved to
an async model using Scala 2.10.x features.  To get the old 2.9.x
version see https://github.com/guardian/pa-football-2.9
*NOTE*: as of 6.0, Scala 2.11 is required

It merely interacts with the PA feeds, it does not understand Guardian
Tags and match reports and so on.

## Breaking Change
From version 6.0.0 this library uses `org.joda.time.LocalDate` instead of the [deprecated](https://github.com/JodaOrg/joda-time/issues/81) `org.joda.time.DateMidnight`

Version 5.0.0 still uses `DateMidnight`

## How to use

Add a resolver to the Bintray Maven repo:

```
resolvers += "Guardian Frontend Bintray" at "http://dl.bintray.com/guardian/frontend"
```

and add a dependency:

```
libraryDependencies += "com.gu" % "pa-client" % "<version>"
```

See the Bintray badge above for the latest published version.

## How to release a new version

1. Make sure you have a Bintray account, it's been added to the Guardian org, and it has permission to publish artifacts to the `frontend` Maven repo.

2. Run `sbt bintrayChangeCredentials` and fill in your username and API key. They will be stored in a file in your `~/.sbt` folder.

3. Bump version number in [version.sbt](version.sbt)

3. Run `sbt publish`. This will publish to Bintray

## Timezones

As of v5.0.0, the PA client correctly uses timezone-aware dates and
times. Since all date information is provided from the PA feed in UK
time, the timezone is set as Europe/London. Your application will need
to be aware of this because Europe/London changes between UTC and
UTC+1 depending on the time of year (BST).

## Usage

Note that this library does not include an HTTP library so you will
need to provide an implementation for the client's GET method.

```scala
object Client extends PaClient {
    val apiKey = "YOUR_API_KEY"
    def GET(url: String): Future[Response] = { /* implement code to fetch a url */ }
}

// list all competitions
Client.competitions.map(_.foreach(println))

// fixtures for all competitions
val fixtures = Client.fixtures()
fixtures.map(_.foreach(println))

// live matches for a competition
val matches = Client.liveMatches("100")
matches.map(_.foreach(println))

// events in a match
val theMatch = Client.matchEvents("3507403")
theMatch.map( m =>  println(m.homeTeam.name))

// matches for a specific day for all competitions
val matches = Client.matchDay(new LocalDate(2011, 8, 27))
matches.map(_.foreach(println))

// matches for a specific day in a specific competition
val matches = Client.matchDay("100", new LocalDate(2011, 8, 27))
matches.map(_.foreach(println))

// results for all competitions since a certain date
val matches = Client.results(new LocalDate(2010, 8, 1))
matches.map(_.foreach(println))

// results for a competition since a certain date
val matches = Client.results("100", new LocalDate(2010, 8, 1))
matches.map(_.foreach(println))

// results for a competition between two dates
val matches = Client.results("100", new LocalDate(2012, 8, 23), new LocalDate(2012, 9, 1))
matches.map(_.foreach(println))

// lineup for a match
val lineup = Client.lineUp("1234")
lineup.map(l => println(l.homeTeam.name))

// team head 2 head data
val teamH2Hs = Client.teamHead2Head("4", "12", new LocalDate(2013, 12, 2), new LocalDate(2014, 1, 24))
teamH2Hs.map { case (team1H2H, team2H2H) =>
  println(s"${team1H2H.name} vs ${team2H2H.name}")
  println(s"${team1H2H.totalGoals} - ${team2H2H.totalGoals}")
}

// show events for a team
val teamEvents = Client.teamEvents("19", new LocalDate(2013, 10, 11), new LocalDate(2014, 1, 24))
teamEvents.map(_.foreach(println))

// results for the specified team
val teamResults = Client.teamResults("19", new LocalDate(2013, 10, 11))
teamResults.map(_.foreach(println))

// get a stats summary for a team
val teamStats = Client.teamStats("19", new LocalDate(2013, 8, 1), new LocalDate(2014, 2, 5))
teamStats.map(println)

// retrieve a team's squad
val squad = Client.squad("19")
squad.map(_.foreach(println))

// get all the teams in the specified competition (Premier League, in this example)
val teams = Client.teams("100", new LocalDate(2013, 12, 5), new LocalDate(2014, 2, 4))
teams.map(_.foreach(team => println(team.name)))

// fetch head to head information for two players
val playerH2H = Client.playerHead2Head("300448", "494151", new LocalDate(2013, 11, 3), new LocalDate(2014, 2, 4), "100")
playerH2H.map { case (player1H2H, player2H2H) =>
  println(s"${player1H2H.name} vs ${player2H2H.name}")
  println(s"${player1H2H.totalGoals} - ${player2H2H .totalGoals}")
}

// get the statistics on appearances for the given player
val playerAppearances = Client.appearances("237670", new LocalDate(2013, 9, 4), new LocalDate(2014, 2, 4))
playerAppearances.map(appearances => s"${appearances.playerName} has appeared ${appearances.total} times")

// fetch a player's profile information
Client.playerProfile("237670").map(profile => s"${profile.fullName}, ${profile.age} years old, ${profile.height} tall")

// stats summary for a player
val playerStats = Client.playerStats("237670", new LocalDate(2013, 8, 1), new LocalDate(2014, 2, 5))
playerStats.map(stats => s"Shots on target: ${stats.offence.shotsOnTargetPercentage.total}%")
```
