import sbt._
import sbt.Keys._

object NeptunAPI extends Build {

  lazy val neptunAPI = Project(
    id = "neptun-api",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Neptun API",
      organization := "hu.gansperger",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.11.2",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies ++= Seq(
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
        "com.typesafe.play" %% "play-json" % "2.3.3",
        "com.github.nscala-time" %% "nscala-time" % "1.2.0",
        "org.jsoup" % "jsoup" % "1.7.2"
      )
    )
  )
}
