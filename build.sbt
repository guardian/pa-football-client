name := "pa-client"

organization := "com.gu"

version := "3.1-SNAPSHOT"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "1.6.2",
    "net.databinder.dispatch" %% "dispatch-core" % "0.9.5",
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
