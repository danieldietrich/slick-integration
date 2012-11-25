organization := "net.danieldietrich"

name := "slick-integration"

version := "1.0"

scalaVersion := "2.10.0-RC2"

scalacOptions += "-deprecation"

libraryDependencies ++= List(
  "com.typesafe" % "slick_2.10.0-RC2" % "0.11.2",
  "com.h2database" % "h2" % "1.3.166",
  "org.slf4j" % "slf4j-nop" % "1.6.4", // <- disables logging
  "play" % "play_2.10" % "2.1-RC1",
  "play" % "play-jdbc_2.10" % "2.1-RC1",
  "org.specs2" %% "specs2" % "1.12.3" % "test"
/*
// enables logging
  ,"org.slf4j" % "slf4j-api" % "1.6.4"
  ,"ch.qos.logback" % "logback-classic" % "0.9.28"
*/
/*
// Other database drivers
  "org.apache.derby" % "derby" % "10.6.1.0",
  "org.hsqldb" % "hsqldb" % "2.0.0",
  "postgresql" % "postgresql" % "8.4-701.jdbc4",
  "mysql" % "mysql-connector-java" % "5.1.13"
*/
)

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
