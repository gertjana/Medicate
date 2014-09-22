import AssemblyKeys._ // put this at the top of the file

name := "Medicate Web"

organization := "net.addictivesoftware.medicate"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

scalacOptions in Test ++= Seq("-Yrangepos")

jarName in assembly := "medicate-spray.jar"

resolvers ++= Seq(
    "Akka" at "http://repo.akka.io/releases/",
    "Spray" at "http://repo.spray.io",
    "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    "Scala Tools" at "https://oss.sonatype.org/content/groups/scala-tools/",
    "Sonatype OSS" at "http://oss.sonatype.org/content/repositories/releases",
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
    "io.spray" % "spray-http" % "1.3.1",
    "io.spray" % "spray-can" % "1.3.1",
    "io.spray" % "spray-io" % "1.3.1",
    "io.spray" % "spray-routing" % "1.3.1",
    "io.spray" %  "spray-json_2.10" % "1.3.0",
    "io.spray" % "spray-testkit" % "1.3.1" % "test",
    "com.typesafe.akka" %% "akka-actor" % "2.3.2",
    "com.typesafe.akka" %% "akka-slf4j" % "2.3.2",
    "org.specs2" %% "specs2" % "2.2.3" % "test",
    "ch.qos.logback" % "logback-classic" % "0.9.27",
    "com.novus" %% "salat-core" % "1.9.9"
)

seq(Revolver.settings: _*)

assemblySettings