import sbtrelease._
import ReleaseStateTransformations._

releaseSettings

sonatypeSettings

name := "pa-client"

organization := "com.gu"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4") // Add more versions into here when Scala 2.11 becomes available?

scalacOptions ++= Seq("-feature", "-deprecation")

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/pa-football-client"),
  "scm:git:git@github.com:guardian/pa-football-client.git"
))

description := "Scala client for PA football feeds. Only does football data, it has no knowledge of Guardian match reports and such."

pomExtra := (
<url>https://github.com/xuwei-k/sbt-sonatype-and-sbt-release-sample</url>
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
    "joda-time" % "joda-time" % "1.6.2",
    "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)

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

