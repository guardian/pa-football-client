package pa

import xml.NodeSeq
import org.joda.time.DateTime


object `package` {

  //some shortcuts to avoid the whole (node \ "@competitonId").text thing
  implicit def nodeSeq2rich(node: NodeSeq) = new {

    //directly gets the text value of an attribute
    def \@(attributeName: String) = (node \ ("@" + attributeName)).text match {
      case null => None
      case "" => None
      case s => Some(s)
    }

    //directly gets the text value of an attribute as an option
    //def \@<(attributeName: String) = Option((\@(attributeName)))

    //directly gets the text value of an element
    def \>(elementsName: String) = (node \ elementsName).text match {
      case null => None
      case "" => None
      case s => Some(s)
    }

    //directly gets the text value of an element as an Option[String]
    //def \<(elementsName: String) = Option(\>(elementsName))
  }

  implicit def date2midnight(date: DateTime) = date.toDateMidnight

  implicit def optionString2String(s: Option[String]) = s.get

}