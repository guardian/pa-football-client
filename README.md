A simple scala client for the PA football API

It merely interacts with the PA feeds, it does not understand Guardian Tags and match reports and so on

Sbt dependencies

    resolvers += "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases"

    libraryDependencies += "com.gu" %% "com.gu.pa-client" % "1.10"

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

    //results for a competition since a certain date
    val matches = Client.results("100", new DateMidnight(2010, 8, 1))
    matches.foreach(println)

    //results for a competition between two dates
    val matches = Client.results("100", new DateMidnight(2012, 8, 23), new DateMidnight(2012, 9, 1))
    matches.foreach(println)