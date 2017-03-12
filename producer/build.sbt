name := "producer"

version := "1.0"

scalaVersion := "2.11.7"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka_2.11
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.3"

// https://mvnrepository.com/artifact/org.codehaus.jettison/jettison
libraryDependencies += "org.codehaus.jettison" % "jettison" % "1.3.8"
