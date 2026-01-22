import ReleaseTransformations._
import sbtversionpolicy.withsbtrelease.ReleaseVersion

val scala_2_12: String = "2.12.21"
val scala_2_13: String = "3.8.1"
val scala_3: String = "3.3.7"

scalaVersion := scala_3
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.scala-lang.modules" %% "scala-xml" % "2.4.0",
  "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2"
)

scalacOptions ++= Seq("-feature", "-deprecation", "-release:11")

// Required Sonatype fields
name := "pa-client"
organization := "com.gu"
description := "Scala client for PA football feeds. Only does football data, it has no knowledge of Guardian match reports and such."
licenses := Seq(License.Apache2)

crossScalaVersions := Seq(scala_3, scala_2_12, scala_2_13)
releaseCrossBuild := true
releaseVersion := ReleaseVersion.fromAggregatedAssessedCompatibilityWithLatestRelease().value

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
)

Test/testOptions += Tests.Argument(
  TestFrameworks.ScalaTest,
  "-u", s"test-results/scala-${scalaVersion.value}", "-o"
)
