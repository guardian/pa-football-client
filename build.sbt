version := "0.1-SNAPSHOT"

resolvers += "xuwei-k repo" at "http://xuwei-k.github.com/mvn"

libraryDependencies ++= Seq(
    "net.liftweb" %% "lift-json" % "2.4",
    "net.liftweb" %% "lift-json-ext" % "2.4",
    "joda-time" % "joda-time" % "1.6.2",
    "org.scalatest" %% "scalatest" % "1.8" % "test"
)