A simple scala client for the PA football API.

*NOTE*: as of 4.0, Scala 2.9.x is no longer as the client has moved to
an async model using Scala 2.10.x features.  To get the old 2.9.x
version see https://github.com/guardian/pa-football-2.9

It merely interacts with the PA feeds, it does not understand Guardian
Tags and match reports and so on.

## Sbt dependencies

To include this library in your project using sbt, add the following
to your sbt configuration:

```scala
resolvers += "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases"

libraryDependencies += "com.gu" %% "pa-client" % "4.0"
```

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
val matches = Client.matchDay(new DateMidnight(2011, 8, 27))
matches.map(_.foreach(println))

// matches for a specific day in a specific competition
val matches = Client.matchDay("100", new DateMidnight(2011, 8, 27))
matches.map(_.foreach(println))

// results for all competitions since a certain date
val matches = Client.results(new DateMidnight(2010, 8, 1))
matches.map(_.foreach(println))

// results for a competition since a certain date
val matches = Client.results("100", new DateMidnight(2010, 8, 1))
matches.map(_.foreach(println))

// results for a competition between two dates
val matches = Client.results("100", new DateMidnight(2012, 8, 23), new DateMidnight(2012, 9, 1))
matches.map(_.foreach(println))

// lineup for a match
val lineup = Client.lineUp("1234")
lineup.map(l => println(l.homeTeam.name))
```
