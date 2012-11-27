import sbt._
import Keys._

object ScalaBuild extends Build {

  val buildOrganization = "net.danieldietrich"
  val buildName = "slick-integration"
  val buildVersion = "1.1-SNAPSHOT"

  val buildScalaVersion = "2.10.0-RC2"
  val buildScalacOptions = Seq("-encoding", "UTF-8", "-target:jvm-1.6", "-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args")

  val buildDependencies = Seq(

    "com.typesafe" % "slick_2.10.0-RC2" % "0.11.2",
    "play" % "play_2.10" % "2.1-RC1",
    "play" % "play-jdbc_2.10" % "2.1-RC1",

    "com.h2database" % "h2" % "1.3.166" % "test",
    "org.slf4j" % "slf4j-nop" % "1.6.4" % "test", // <- disables logging
    "org.specs2" %% "specs2" % "1.12.3" % "test")

  val buildResolvers = Seq(
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/")

  val projectSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions := buildScalacOptions,
    libraryDependencies := buildDependencies,
    resolvers := buildResolvers,
    
    publishMavenStyle := true,
    publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/.m2/repository"))))

  val main = Project(
    "slick-integration",
    file("."),
    settings = projectSettings)

}
