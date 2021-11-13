import jdk.javadoc.internal.tool.resources.javadoc

name := """RedditLytics"""
organization := "com.zeroerrors"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.13"
crossScalaVersions := Seq("2.11.12", "2.13.6")

libraryDependencies += guice
libraryDependencies += ws
// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// Javadoc
Compile / doc / javacOptions ++= Seq("-notimestamp", "-linksource")

//Compile /doc/sources   ~= (_.filterNot(_.getName endsWith ".scala"))

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"



