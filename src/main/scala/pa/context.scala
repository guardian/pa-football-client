package pa

import xml.NodeSeq
import org.joda.time.DateTime


object `package` {

  //some shortcuts to avoid the whole (node \ "@competitonId").text thing
  implicit def nodeSeq2rich(node: NodeSeq) = new {

    //directly gets the text value of an attribute
    def \@(attributeName: String) = (node \ ("@" + attributeName)).text

    //directly gets the text value of an element
    def \>(elementsName: String) = (node \ elementsName).text
  }

  implicit def date2midnight(date: DateTime) = date.toDateMidnight

}