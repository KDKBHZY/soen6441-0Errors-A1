name := """RedditLytics"""
organization := "com.zeroerrors"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += ws
//libraryDependencies += "org.webjars" % "jquery" % "3.3.1-2"

