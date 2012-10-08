name := "pa-client"

organization := "com.gu"

version := "2.2-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "1.6.2",
    "net.databinder.dispatch" %% "core" % "0.9.0",
    "org.scalatest" %% "scalatest" % "1.8" % "test"
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
