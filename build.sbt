import ReleaseTransformations._

val scala_2_12: String = "2.12.11"
val scala_2_13: String = "2.13.2"

scalaVersion := scala_2_12

name := "pa-client"

organization := "com.gu"

description := "Scala client for PA football feeds. Only does football data, it has no knowledge of Guardian match reports and such."

crossScalaVersions := Seq(scala_2_12, scala_2_13)
releaseCrossBuild := true
publishMavenStyle := true
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

bintrayOrganization := Some("guardian")
bintrayRepository := "frontend"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.2" % "test"
)

scalacOptions ++= Seq("-feature", "-deprecation")

releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  releaseStepTask(bintrayRelease),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
