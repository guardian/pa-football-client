name := "pa-client"

organization := "com.gu"

version := "4.4"

scalaVersion := "2.10.0"

crossScalaVersions := Seq("2.10.0", "2.10.1")

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "1.6.2",
    "org.scalatest" %% "scalatest" % "1.9.1" % "test"
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
