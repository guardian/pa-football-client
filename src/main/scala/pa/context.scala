package pa

import xml.NodeSeq
import org.joda.time.{DateMidnight, DateTime}


object `package` {

  //some shortcuts to avoid the whole (node \ "@competitonId").text thing
  implicit def nodeSeq2rich(node: NodeSeq) = new {

    //directly gets the text value of an attribute
    def \@(attributeName: String) = (node \ ("@" + attributeName)).text match {
      case null => None
      case "" => None
      case s => Some(s)
    }

    //directly gets the text value of an element
    def \>(elementsName: String) = (node \ elementsName).text match {
      case null => None
      case "" => None
      case s => Some(s)
    }
  }

  implicit def date2midnight(date: DateTime): DateMidnight = date.toDateMidnight

  implicit def optionString2String(s: Option[String]): String = s.get

  implicit def optionString2boolean(s: Option[String]): Boolean = s match {
    case Some("Yes") => true
    case Some("No") => false
    case _ => throw new RuntimeException("Unexpected value for boolean: " + s)
  }

  implicit def optionString2int(s: Option[String]) = new {
    lazy val toInt = s.get.toInt
  }
}