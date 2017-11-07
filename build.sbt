enablePlugins(GitVersioning)

scalaVersion := "2.12.4"

name := "pa-client"

organization := "com.gu"

description := "Scala client for PA football feeds. Only does football data, it has no knowledge of Guardian match reports and such."

publishMavenStyle := true

bintrayOrganization := Some("guardian")

bintrayRepository := "frontend"

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.6",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.1.3" % "test"
)


scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
      )
    case _ => libraryDependencies.value
  }
}
