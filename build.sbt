name := "pa-client"

organization := "com.gu"

version := "4.7-SNAPSHOT"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.0", "2.10.1", "2.10.2", "2.10.3")

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "2.2",
    "org.joda" % "joda-convert" % "1.6",
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
