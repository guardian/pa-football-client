A simple scala client for the PA football API

It merely interacts with the PA feeds, it does not understand Guardian Tags and match reports and so on

Sbt dependencies

    resolvers += "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases"

    libraryDependencies += "com.gu" %% "pa-client" % "1.6"

Usage

    object Client extends PaClient with DispatchHttp {
        val apiKey = "YOUR_API_KEY"
    }

    //list all competitions
    Client.competitions.foreach(println)

    //events in a match
    val theMatch = Client.matchEvents("3507403")
    println(theMatch.homeTeam.name)

    //matches for a specific day in a specific competition
    val matches = Client.matchDay("100", new DateMidnight(2011, 8, 27))
    matches.foreach(println)