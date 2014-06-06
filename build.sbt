import sbtrelease._
import ReleaseStateTransformations._

releaseSettings

sonatypeSettings

name := "pa-client"

organization := "com.gu"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.1")

scalacOptions ++= Seq("-feature", "-deprecation")

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/pa-football-client"),
  "scm:git:git@github.com:guardian/pa-football-client.git"
))

description := "Scala client for PA football feeds. Only does football data, it has no knowledge of Guardian match reports and such."

pomExtra := (
<url>https://github.com/guardian/pa-football-client</url>
<developers>
  <developer>
    <id>adamnfish</id>
    <name>Adam Fisher</name>
    <url>https://github.com/adamnfish</url>
  </developer>
</developers>
)

licenses := Seq("Apache V2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))



libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.6",
  "org.scalatest" %% "scalatest" % "2.1.7" % "test"
)

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-xml" % "1.0.2"
      )
    case _ => libraryDependencies.value
  }
}

ReleaseKeys.releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(
    action = state => Project.extract(state).runTask(PgpKeys.publishSigned, state)._1,
    enableCrossBuild = true
  ),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(state => Project.extract(state).runTask(SonatypeKeys.sonatypeReleaseAll, state)._1),
  pushChanges
)

