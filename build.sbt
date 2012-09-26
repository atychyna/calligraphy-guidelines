// set the name of the project
name := "calligraphy-guidelines"

version := "1.0"

organization := "org.ant"

// set the Scala version used for the project
scalaVersion := "2.9.2"

seq(webSettings :_*)

classpathTypes ~= (_ + "orbit")

libraryDependencies ++= Seq(
  "org.scalatra" % "scalatra" % "2.1.1",
  "org.scalatra" % "scalatra-scalate" % "2.1.1",
  "org.scalatra" % "scalatra-specs2" % "2.1.1" % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
)

libraryDependencies ++= Seq(
    "net.databinder.dispatch" %% "core" % "0.9.1",
    "org.apache.pdfbox" % "pdfbox" % "1.7.1",
    "org.scalatest" %% "scalatest" % "1.8" % "test"
)
