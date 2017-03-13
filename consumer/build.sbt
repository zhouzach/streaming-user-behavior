import sbt.Keys.libraryDependencies

name := "consumer"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  // https://mvnrepository.com/artifact/org.apache.spark/spark-streaming_2.11
  "org.apache.spark" % "spark-streaming_2.11" % "2.1.0",

  // https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka_2.11
  "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.3",

  // https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
  "org.apache.commons" % "commons-pool2" % "2.4.2",

  // https://mvnrepository.com/artifact/redis.clients/jedis
  "redis.clients" % "jedis" % "2.9.0",

  "com.typesafe" % "config" % "1.3.1",

  // https://mvnrepository.com/artifact/net.sf.ezmorph/ezmorph
  "net.sf.ezmorph" % "ezmorph" % "1.0.4"
)

