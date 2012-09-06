import xml.NodeSeq
import org.joda.time.{DateMidnight, DateTime}

package object pa{

  implicit def seq2List[A](s: Seq[A]) = s.toList

  implicit def string2Option (s: String): Option[String] = s match {
    case null => None
    case "" => None
    case _ => Some(s)
  }

  //some shortcuts to avoid the whole (node \ "@competitonId").text thing
  implicit def nodeSeq2rich(node: NodeSeq) = new {

    //directly gets the text value of an attribute
    def \@(attributeName: String): String = (node \ ("@" + attributeName)).text

    def \@@(attributeName: String): Option[String] = {
      (node \ ("@" + attributeName)).text match {
        case s if s.length > 0  => Some(s)
        case _                  => None
      }
    }

    //directly gets the text value of an element
    def \>(elementsName: String): String = (node \ elementsName).text

    def \>>(elementsName: String): Option[String] = {
      (node \ elementsName).text match {
        case s if s.length > 0  => Some(s)
        case _                  => None
      }
    }
  }

  implicit def date2midnight(date: DateTime): DateMidnight = date.toDateMidnight

  implicit def optionString2boolean(s: Option[String]): Boolean = s match {
    case Some("Yes") => true
    case Some("No") => false
    case _ => throw new RuntimeException("Unexpected value for boolean: " + s)
  }

  implicit def optionString2int(s: Option[String]) = new {
    lazy val toInt = s.get.toInt
  }
}