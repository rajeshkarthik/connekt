import AppBuild._
import sbt.ExclusionRule

name := "commons"

version := "1.0-SNAPSHOT"

envKey := {
  System.getProperty("env.key") match {
    case null => "default"
    case defined: String => defined
  }
}


/** all akka only **/
val akkaVersion = "2.4.2"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-stream" % akkaVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion withSources(),
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaVersion.concat("-RC2") % Test withSources() withJavadoc()
)

libraryDependencies ++= Seq(
  /* Logging Dependencies.Since we want to use log4j2 */
  "com.lmax" % "disruptor" % "3.3.4",
  "org.apache.logging.log4j" 			% "log4j-api" 			  % "2.5",
  "org.slf4j"                     % "slf4j-api"         % "1.7.13",
  "org.apache.logging.log4j" 			% "log4j-core" 			  % "2.5",
  "org.apache.logging.log4j" 			% "log4j-1.2-api"		  % "2.5",    /* For Log4j 1.x API Bridge*/
  "org.apache.logging.log4j" 			% "log4j-jcl"	    	  % "2.5",    /* For Apache Commons Logging Bridge */
  "org.apache.logging.log4j" 			% "log4j-slf4j-impl"  % "2.5",    /* For SLF4J Bridge */
  /* End of Logging Libraries dependency */
  "commons-pool" % "commons-pool" % "1.6",
  "org.apache.kafka" % "kafka_2.11" % "0.8.2.2" excludeAll
    ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12"),
  "com.typesafe" % "config" % "1.3.0",
  "commons-beanutils" % "commons-beanutils" % "1.8.0",
  "org.ow2.asm" % "asm" % "4.1",
  "com.esotericsoftware" % "kryo-shaded" % "3.0.3",
  "org.apache.hbase" % "hbase" % "0.94.15-cdh4.7.0" excludeAll(
    ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12"),
    ExclusionRule(organization = "javax.servlet"),
    ExclusionRule(organization = "javax.servlet.jsp"),
    ExclusionRule(organization = "tomcat"),
    ExclusionRule(organization = "org.eclipse.jetty.orbit"),
    ExclusionRule(organization = "log4j"),
    ExclusionRule(organization = "org.jruby", name = "jruby-complete"),
    ExclusionRule(organization = "org.slf4j"),
    ExclusionRule(organization = "org.mortbay.jetty", name = "servlet-api-2.5"),
    ExclusionRule(organization = "org.mockito"),
    ExclusionRule(organization = "junit"),
    ExclusionRule(organization = "asm"),
    ExclusionRule(organization = "javax.xml.stream", name = "stax-api"),
    ExclusionRule(organization = "com.google.guava", name = "guava"),
    ExclusionRule(organization = "jline", name = "jline")
    ),
  "org.apache.hadoop" % "hadoop-client" % "2.0.0-mr1-cdh4.7.0" excludeAll(
    ExclusionRule(organization = "javax.servlet"),
    ExclusionRule(organization = "tomcat"),
    ExclusionRule(organization = "javax.servlet.jsp"),
    ExclusionRule(organization = "org.eclipse.jetty.orbit"),
    ExclusionRule(organization = "log4j"),
    ExclusionRule(organization = "org.mockito"),
    ExclusionRule(organization = "junit"),
    ExclusionRule(organization = "asm"),
    ExclusionRule(organization = "org.slf4j"),
    ExclusionRule(organization = "org.jruby", name = "jruby-complete"),
    ExclusionRule(organization = "org.mortbay.jetty", name = "servlet-api-2.5"),
    ExclusionRule(organization = "javax.xml.stream", name = "stax-api"),
    ExclusionRule(organization = "com.google.guava", name = "guava"),
    ExclusionRule(organization = "jline", name = "jline"),
    ExclusionRule(organization = "commons-beanutils")
    ),

  "org.scalatest" % "scalatest_2.11" % "2.2.4" % Test,
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.3",
  "commons-lang" % "commons-lang" % "2.6",
  "commons-pool" % "commons-pool" % "1.6",
  "org.springframework" % "spring-jdbc" % "4.2.3.RELEASE",
  "org.apache.commons" % "commons-dbcp2" % "2.1.1",
  "javax.persistence" % "persistence-api" % "1.0.2",
  "mysql" % "mysql-connector-java" % "5.1.37",
  "com.h2database" % "h2" % "1.4.187" % Test,
  "com.google.guava" % "guava" % "19.0",
  "org.apache.velocity" % "velocity" % "1.7",
  "org.codehaus.groovy" % "groovy-all" % "2.4.5",
  "com.roundeights" %% "hasher" % "1.2.0",
  "com.couchbase.client" % "java-client" % "2.1.3",
  "io.reactivex" %% "rxscala" % "0.26.0",
  "org.apache.curator" % "curator-recipes" % "2.9.1",
  "com.flipkart.specter" % "specter-client" % "1.4.0-SNAPSHOT",
  "joda-time" % "joda-time" % "2.3",
  "com.flipkart" %% "util-config" % "0.0.1",
  "com.flipkart" %% "espion" % "1.0.1",
  "com.flipkart" %% "util-http" % "0.0.1-SNAPSHOT"
)


test in assembly := {}

parallelExecution in Test := false


assemblyMergeStrategy in assembly := AppBuild.mergeStrategy
