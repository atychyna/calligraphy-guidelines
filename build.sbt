// set the name of the project
name := "calligraphy-guidelines"

version := "1.0"

organization := "org.ant"

// set the Scala version used for the project
scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "net.databinder.dispatch" %% "core" % "0.9.1",
    "org.apache.pdfbox" % "pdfbox" % "1.7.1",
    "org.scalatest" %% "scalatest" % "1.8" % "test"
)
