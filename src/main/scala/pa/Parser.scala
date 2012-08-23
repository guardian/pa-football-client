package pa

import net.liftweb.json._
import net.liftweb.json.ext.JodaTimeSerializers
import java.util.Date
import java.text.SimpleDateFormat
import scala.Some

//There is always a certain amount of ugliness in parsing a feed.
//keep it all in one place
object Parser {

  import JsonCleaner._

  implicit val formats = new DefaultFormats{

    private val DateOnly = """^(\d\d/\d\d/\d\d\d\d)$""".r

    //turns out SimpleDateFormat is not thread safe
    //http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4228335
    //do not convert this to a val...
    private def dateOnlyFormat =  new SimpleDateFormat("dd/MM/yyyy")

    override val dateFormat = new DateFormat {
      override def parse(s: String): Option[Date] =
        s match {
          case DateOnly(_) => Some(dateOnlyFormat.parse(s))
          case _ => None
        }

      override def format(d: Date): String = throw new RuntimeException("not expecting to output dates")
    }
  } ++ JodaTimeSerializers.all


  def parseCompetitions(s: String): List[Season] = (parse(JsonCleaner(s)) \\ "season").extract[List[Season]]

  private val text2Name: Map[String, String] = Map("text" -> "name")

  def parseMatchEvents(s: String): MatchEvents = {
    val json = parse(JsonCleaner(s, text2Name))
    MatchEvents(
      (json \\ "homeTeam").extract[Team],
      (json \\ "awayTeam").extract[Team],
      (json \\ "event").transform{players2player}.extract[List[Event]]
    )
  }

  private val statsMapping: Map[String, String] = text2Name + ("possession" -> "homePossession")

  def parseMatchStats(s: String): MatchStats = {
    val json = parse(JsonCleaner(s, statsMapping)).children.head
    json.transform{string2int}.extract[MatchStats]
  }

  private val matchDayMapping: Map[String, String] = text2Name + ("refereeID" -> "id")

  def parseMatchDay(s: String): List[MatchDay] = {
    val json = parse(JsonCleaner(s, matchDayMapping)).transform { string2int }.transform{yesNo2boolean}
    json \\ "match" match {
      case JObject(Nil) => Nil
      case obj: JObject => List(obj.extract[MatchDay]) // Handles single match
      case array => array.extract[List[MatchDay]] // Handles days with multiple matches
    }
  }

  private val leagueMapping = Map("for" -> "goalsFor", "against" -> "goalsAgainst")

  def parseLeagueTable(s: String): List[LeagueTableEntry] = {
    val json = parse(JsonCleaner(s, leagueMapping)).transform{string2int}
    (json \\ "tableEntry").extract[List[LeagueTableEntry]]
  }

}


//PA feed converts from XML and you get some weirdness such as attributes get an @
object JsonCleaner {

  /**
   * Strips out '@' and '#' symbols and replaces specified keys with alternatives (e.g. changes 'for' to 'goalsFor').
   */
  def apply(s: String, mappings: Map[String, String] = Map.empty): String =
    mappings.foldLeft(s.replace("\"@", "\"").replace("\"#", "\"")) { case (z, (k, v)) =>
      z.replaceAllLiterally("\"%s\"" format k, "\"%s\"" format v)
    }

  val IntPattern = """^(\d+)$""".r

  def players2player: PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField(("players"), JObject(List(JField("player1", player1), JField("player2", player2)))) =>
      val players = List(player1, player2).filter(p => (p \ "playerID").values.asInstanceOf[String] != "")
      JField("players", JArray(players))
  }

  def string2int: PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField(name, JString(IntPattern(value))) => JField(name, JInt(value.toInt))
  }

  def yesNo2boolean:  PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField(name, JString("Yes")) => JField(name, JBool(true))
    case JField(name, JString("No")) => JField(name, JBool(false))
  }
}