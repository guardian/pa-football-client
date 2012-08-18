package pa

import net.liftweb.json._
import ext.JodaTimeSerializers
import java.util.Date
import java.text.SimpleDateFormat


object Parser {

  implicit val formats = new DefaultFormats{

    val DateOnly = """^(\d\d/\d\d/\d\d\d\d)$""".r

    val dateOnlyFormat =  new SimpleDateFormat("dd/MM/yyyy")

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

}

//PA feed converts from XML and attributes get an @
object JsonCleaner {
  def apply(s: String) = s.replace("@competitionID", "competitionID")
}
