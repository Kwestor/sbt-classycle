sbtPlugin := true

name := "sbt-classycle"

organization := "org.virtuslab"

version := "0.1"

scalaVersion := "2.10.3"

// cyclic dependencies analyzer
libraryDependencies += "org.specs2" % "classycle" % "1.4.1"