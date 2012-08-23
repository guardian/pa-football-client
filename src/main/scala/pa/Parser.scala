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


  def parseCompetitions(s: String) = ((parse(JsonCleaner(s)) \\ "season").children).map(_.extract[Season])

  def parseMatchEvents(s: String) = {
    val json = parse(JsonCleaner(s))

    MatchEvents(
      (json \\ "homeTeam").transform{text2name}.extract[Team],
      (json \\ "awayTeam").transform{text2name}.extract[Team],
      (json \\ "events" \ "event").children
        .map{_.transform{players2player}.transform{text2name}}
        .map{_.extract[Event]}
    )
  }

  def parseMatchStats(s: String) = {
    val json = parse(JsonCleaner(s)).transform{string2int}
    MatchStats(
    (json \\ "possession").extract[Int],
    (json \\ "homeTeam").transform{string2int}.extract[TeamStats],
    (json \\ "awayTeam").transform{string2int}.extract[TeamStats]
    )
  }


  def parseMatchDay(s: String) = {
    val json = parse(JsonCleaner(s)).transform { string2int } \\ "match" match {
      //handles days with only a single match
      case obj: JObject => JArray(List(obj))
      case array => array
    }

    //the no data found error lives under matches
    //so filter out any empty children
    json.children.filterNot(_.children.isEmpty).map{
       _.transform{yesNo2boolean}
       .transform{text2name}
       .transform{refereeId2id}
       .extract[MatchDay]
    }
  }
}


//PA feed converts from XML and you get some weirdness such as attributes get an @
object JsonCleaner {
  def apply(s: String) = s.replace("\"@", "\"").replace("\"#", "\"")

  val IntPattern = """^(\d+)$""".r

  //these rename fields, once again due to XML conversion you can get a #text where you want a
  //decent name
  def text2name: PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField("text", JString(x)) => JField("name", JString(x))
  }

  def players2player: PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField(("players"), JObject(List(JField("player1", player1), JField("player2", player2)))) =>
      val players = List(player1, player2).filter(p => (p \ "playerID").values.asInstanceOf[String] != "")
      JField("players", JArray(players))
  }

  def string2int: PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField(name, JString(IntPattern(value))) => JField(name, JInt(value.toInt))
  }

  def refereeId2id: PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField("refereeID", value) => JField("id", value)
  }

  def yesNo2boolean:  PartialFunction[JsonAST.JValue, JsonAST.JValue] = {
    case JField(name, JString("Yes")) => JField(name, JBool(true))
    case JField(name, JString("No")) => JField(name, JBool(false))
  }
}