import ReleaseTransformations._

val scala_2_12: String = "2.12.17"
val scala_2_13: String = "2.13.10"
val scala_3: String = "3.3.0"

scalaVersion := scala_3
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.16" % Test,
  "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
  "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2"
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

crossScalaVersions := Seq(scala_3, scala_2_12, scala_2_13)
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

Test/testOptions += Tests.Argument(
  TestFrameworks.ScalaTest,
  "-u", s"test-results/scala-${scalaVersion.value}", "-o"
)
