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
      scalaVersion := "2.10.3",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies ++= Seq(
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"
      )
    )
  )
}