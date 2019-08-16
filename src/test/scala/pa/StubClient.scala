package pa

import java.io.{File, PrintWriter}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.io.Source
import scala.language.postfixOps
import scala.util.control.NonFatal

object StubClient extends PaClient with Http {

  override val apiKey = "key"

  override def GET(url: String): Future[Response] = readOrFetch(url)

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val wsClient = StandaloneAhcWSClient()


  def readOrFetch(url: String)(implicit context: ExecutionContext): Future[Response] = {

    val filename = s"src/test/resources/data/${url.replaceFirst(base+"/", "")}.xml"
    val urlWithKey = url.replace("/key", "/key")

    readFromFile(filename).recoverWith {
      case e: Exception =>
        println(s"Missing fixture for API response: $url ($filename)")
        val response: Future[Response] = fetchFromUrl(urlWithKey)
        response.foreach { r =>
          println(s"Writing response to test files, $filename")
          writeToFile(filename, r.body)
        }
        response
    }
  }

  def fetchFromUrl(url: String): Future[Response] = {
    wsClient.url(url).withRequestTimeout(10000 milli)
      .get()
      .map { wsResponse =>
        pa.Response(wsResponse.status, wsResponse.body, wsResponse.statusText)
      }
      .recoverWith {
        case NonFatal(exception) =>
          println(s"Error fetching content for $url", exception)
          Future.failed(exception)
      }
  }

  def readFromFile(filePath: String): Future[Response] = {
    val file = new File(filePath)
    if(file.canRead) {
      val xml = Source.fromFile(file, "UTF-8").getLines().mkString
      Future(Response(200, xml, ""))
    } else {
      Future.failed(new Exception(s"File $filePath cannot be read"))
    }
  }

  def writeToFile(filePath: String, contents: String): Unit = {
    val file = new File(filePath)
    file.getParentFile().mkdirs()
    val writer = new PrintWriter(filePath)
    try writer.write(contents) finally writer.close()
  }
}