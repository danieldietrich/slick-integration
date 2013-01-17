# Slick Integration #

[![Build Status](https://api.travis-ci.org/danieldietrich/slick-integration.png)](https://travis-ci.org/danieldietrich/slick-integration)

helps you to implement a data access layer in your application using [Slick](http://slick.typesafe.com).

**Key Features**

* Designed to be the foundation of [non-anemic](http://martinfowler.com/bliki/AnemicDomainModel.html) domain models
* Providing a thin abstraction layer to reduce boilerplate
* Integrating with [Play Framework](http://www.playframework.org)

## Installation ##

Add ```"net.danieldietrich" %% "slick-integration" % "1.0-SNAPSHOT"``` to your dependencies (project/Build.scala).

You can use the following resolvers:

* ```"http://danieldietrich.net/repository/releases"``` _(currently none, pending until Scala 2.10 final is released)_
* ```"http://danieldietrich.net/repository/snapshots"```

## Contribution ##

Checkout the project via [git](http://git-scm.com):

```shell
$ git clone git@github.com:danieldietrich/slick-integration.git
```

Create [Eclipse](http://www.eclipse.org) project files via [sbt](http://www.scala-sbt.org):

```shell
$ sbt eclipse
```

There are a few more helpful sbt commands:

* ```compile``` compiles the project
* ```test``` runs the [specs2](http://etorreborre.github.com/specs2/) tests located in src/test/scala
* ```publish``` publishes the project to the local [Maven](http://maven.apache.org) repository (~/.m2/repository)

Development versions of slick-integration can be used in your App by setting ```resolvers += Resolver.mavenLocal``` (project/Build.scala), which loads the dependency you pblished to your local Maven repository ~/.m2/repository.
