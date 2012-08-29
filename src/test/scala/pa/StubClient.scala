package pa

import io.Source

object StubClient extends PaClient with Http {

  override lazy val base: String = ""
  override val apiKey = "key"

  override def GET(url: String): Response = Response(200, load(url.replaceFirst("/", "")), "")

  private def load(path: String ) = {
    val resource = getClass.getClassLoader.getResourceAsStream(path + ".xml")
    Source.fromInputStream(resource).mkString
  }
}