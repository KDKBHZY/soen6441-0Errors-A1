name := """RedditLytics"""
organization := "com.zeroerrors"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += ws
// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// Javadoc
sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java"))

libraryDependencies += "org.mockito" % "mockito-core" % "4.0.0" % "test"

libraryDependencies += "junit" % "junit" % "4.13.2" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"