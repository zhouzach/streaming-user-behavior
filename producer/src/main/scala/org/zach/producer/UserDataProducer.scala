package org.zach.producer

import java.util.{Properties, Random}

import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import org.codehaus.jettison.json.JSONObject

/**
  * run by java -jar CollectDataSets.jar
  * Note:
  * running by spark-submit --class CollectUserBehavior CollectDataSets.jar
  * makes no sense because the App is created by spark API
  */
object UserDataProducer {
  private val users = Array(
    "4A4D769EB9679C054DE81B973ED5D768", "8dfeb5aaafc027d89349ac9a20b3930f",
    "011BBF43B89BFBF266C865DF0397AA71", "f2a8474bf7bd94f0aabbd4cdd2c06dcf",
    "068b746ed4620d25e26055a9f804385f", "97edfc08311c70143401745a03a50706",
    "d7f141563005d1b5d0d3dd30138f3f62", "c8ee90aade1671a21336c721512b817a",
    "6b67c8c700427dee7552f81f3228c927", "a95f22eabc4fd4b580c011a3161a9d9d"
  )

  private var pointer = -1

  def getUserId(): String = {
    pointer += 1
    if (pointer >= users.length) pointer = 0
    users(pointer)
  }

  private val clickCount = new Random()

  def getClickCount(): Int = {
    clickCount.nextInt(10)
  }

  def getPhoneType(): String = {
    if (clickCount.nextInt(10) % 2 == 0) "Android"
    else "iOS"
  }

  def main(args: Array[String]): Unit = {
    /** create brokers */
    val brokers =
      if (args.length == 0) "localhost:9092"
      else args(0)

    /** set topic */
    val topic =
      if (args.length <= 1) "hotSpot"
      else args(1)

    /** config and create Producer */
    val prop = new Properties()
    prop.setProperty("metadata.broker.list", brokers)
    prop.setProperty("serializer.class", "kafka.serializer.StringEncoder")
    val kafkaProducerConfig = new ProducerConfig(prop)
    val kafkaProducer = new Producer[String, String](kafkaProducerConfig)

    while (true) {

      /**
        * JSONObject uses LinkedHashMap(default LinkedHashMap<Object, Object>)
        * to store data
        * JSONObject implements Serializable
        */
      val userBehavior = new JSONObject()
      userBehavior
        .put("uid", getUserId())
        .put("time", System.currentTimeMillis().toString())
        .put("phoneType", getPhoneType())
        .put("clickCount", getClickCount())

      /** kafkaProducer send data to the specific topic of the specific broker */
      kafkaProducer
        .send(new KeyedMessage[String, String](topic, userBehavior.toString()))

      println("Message sent:" + userBehavior.toString())

      /** speed of producing,unit is millisecond */
      Thread.sleep(200)
    }

  }
}