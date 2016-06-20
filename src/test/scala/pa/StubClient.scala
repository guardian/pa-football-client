package pa

import concurrent.Future
import concurrent.ExecutionContext
import concurrent.ExecutionContext.Implicits.global
import java.io.{File, PrintWriter}
import play.api.Logger
import play.api.libs.ws.ning.NingWSClient
import scala.io.Source
import scala.util.control.NonFatal

object StubClient extends PaClient with Http {

  override val apiKey = "key"

  override def GET(url: String): Future[Response] = readOrFetch(url)

  val wsClient = NingWSClient()


  def readOrFetch(url: String)(implicit context: ExecutionContext): Future[Response] = {

    val filename = s"src/test/resources/${url.replaceFirst(base+"/", "")}.xml"
    val urlWithKey = url.replace("/key", "/_YOUR_API_KEY_HERE")

    readFromFile(filename).recoverWith {
      case e: Exception =>
        Logger.warn(s"Missing fixture for API response: $url ($filename)")
        val response: Future[Response] = fetchFromUrl(urlWithKey)
        response.onSuccess {
          case r: Response =>
            Logger.info(s"Writing response to test files, $filename.xml")
            writeToFile(filename, r.body)
        }
        response
    }
  }

  def fetchFromUrl(url: String): Future[Response] = {
    wsClient.url(url).withRequestTimeout(10000)
      .get()
      .map { wsResponse =>
        pa.Response(wsResponse.status, wsResponse.body, wsResponse.statusText)
      }
      .recoverWith {
        case NonFatal(exception) =>
          Logger.error(s"Error fetching content for $url", exception)
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