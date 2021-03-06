import sbt._
import Keys._

object ScalaBuild extends Build {

  val buildOrganization = "net.danieldietrich"
  val buildName = "slick-integration"
  val buildVersion = "1.0-SNAPSHOT"

  val buildScalaVersion = "2.10.0"
  val buildScalacOptions = Seq("-encoding", "UTF-8", "-target:jvm-1.6", "-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args")

  val buildDependencies = Seq(
    "com.typesafe" %% "slick" % "1.0.0-RC1",
    "com.h2database" % "h2" % "1.3.170" % "test",
    "org.slf4j" % "slf4j-nop" % "1.7.2" % "test", // <- disables logging
    "org.specs2" %% "specs2" % "1.13" % "test")

  val buildResolvers = Seq(
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/")

  val projectSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions := buildScalacOptions,
    libraryDependencies := buildDependencies,
    resolvers := buildResolvers,
    // 'sbt publish' to local maven repository
    publishMavenStyle := true,
    publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/.m2/repository"))))

  val main = Project(
    buildName,
    file("."),
    settings = projectSettings)

}