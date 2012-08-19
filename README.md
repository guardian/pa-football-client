A simple scala client for the PA football API

It merely interacts with the PA feeds, it does not understand Guardian Tags and match reports and so on

Usage...

    object Client extends PaClient with DispatchHttp {
        val apiKey = "YOUR_API_KEY"
    }

    Client.competitions.foreach(println)

    val theMatch = Client.footballMatch("1234")
    println(theMatch.homeTeam.name)