import ReleaseTransformations._

val scala_2_12: String = "2.12.13"
val scala_2_13: String = "2.13.5"

scalaVersion := scala_2_12
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.2" % "test"
)

scalacOptions ++= Seq("-feature", "-deprecation")

// Required Sonatype fields
name := "pa-client"
organization := "com.gu"
description := "Scala client for PA football feeds. Only does football data, it has no knowledge of Guardian match reports and such."
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))
scmInfo := Some(ScmInfo(
  browseUrl = url("https://github.com/guardian/pa-football-client"),
  connection = "scm:git@github.com:guardian/pa-football-client")
)
homepage := scmInfo.value.map(_.browseUrl)
developers := List(Developer(id = "guardian", name = "Guardian", email = null, url = url("https://github.com/guardian")))
publishTo := sonatypePublishToBundle.value

crossScalaVersions := Seq(scala_2_12, scala_2_13)
releaseCrossBuild := true
publishMavenStyle := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges,
)
