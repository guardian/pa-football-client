package pa

import java.io.{File, PrintWriter}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.language.postfixOps
import scala.util.control.NonFatal
import scala.concurrent.ExecutionContext.Implicits.global
import java.net.http.HttpClient
import java.time.Duration
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.HttpRequest
import java.net.URI
import scala.compat.java8.FutureConverters.toScala

object StubClient extends PaClient with Http {

  override val apiKey = "key"

  override def GET(url: String): Future[Response] = readOrFetch(url)

  val httpClient: HttpClient =
    HttpClient
      .newBuilder
      .connectTimeout(Duration.ofSeconds(10))
      .build

  def readOrFetch(url: String): Future[Response] = {

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
    val request = HttpRequest.newBuilder(URI.create(url)).GET().build

    toScala(httpClient.sendAsync(request, BodyHandlers.ofString))
      .map { response =>
        pa.Response(response.statusCode, response.body, "")
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