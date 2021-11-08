name := """RedditLytics"""
organization := "com.zeroerrors"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += ws


// Javadoc
sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java"))
crossPaths := false
libraryDependencies ++= Seq(
  "junit" % "junit" % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test exclude("junit", "junit-dep")
)