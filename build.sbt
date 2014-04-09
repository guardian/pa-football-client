name := "pa-client"

organization := "com.gu"

version := "4.7-SNAPSHOT"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4") // Add more versions into here when Scala 2.11 becomes available?

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "1.6.2",
    "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)

publishTo <<= (version) { version: String =>
    val publishType = if (version.endsWith("SNAPSHOT")) "snapshots" else "releases"
    Some(
        Resolver.file(
            "guardian github " + publishType,
            file(System.getProperty("user.home") + "/guardian.github.com/maven/repo-" + publishType)
        )
    )
}
