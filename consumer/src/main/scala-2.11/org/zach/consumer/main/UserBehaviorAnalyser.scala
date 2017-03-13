package org.zach.consumer.main

import _root_.kafka.serializer.StringDecoder
import net.sf.json.JSONObject
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils
import org.zach.consumer.model.Record
import org.zach.consumer.redisDao.RecordRedisDao


object UserBehaviorAnalyser {

  def main(args: Array[String]): Unit = {
    val masterUrl =
      if (!args.isEmpty) args(0)
      else "local[4]"

    val conf = new SparkConf()
      .setMaster(masterUrl)
      .setAppName("analyse user behavior")

    /**
      * Seconds():batch processing time, 5s
      */
    val ssc = new StreamingContext(conf, Seconds(5))

    /** the topic has to be same with the client's topic */
    val topics = if (args.length <= 1) Set("hotSpot") else Set(args(1))
    val brokers = "localhost:9092"

    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers
    )

    /** create kafkaStream by Spark Streaming API */
    val kafkaStream = KafkaUtils.createDirectStream[String, String,
      StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    /** line._1 is null */
    val events = kafkaStream.flatMap(line => {
      val data = JSONObject.fromObject(line._2)
      Some(data)
    })

    /** because execute reduceByKey,"clickCount" have to use getLong() */
    val userClicks = events.map(x => (x.getString("uid"), x.getLong("clickCount")))
      .reduceByKey(_ + _)

    /** write the result into Redis */
    userClicks.foreachRDD(rdd => {
      rdd.foreachPartition(RecordsofPartition => {

        /** the Jedis instance has to be gotten in the PARTITION */
        val recordRedisDao = new RecordRedisDao

        RecordsofPartition.foreach(pair => {
          val uid = pair._1
          val clickCount = pair._2
          val record = Record(uid, clickCount)

          /** write the pair into Redis */
          recordRedisDao.insertRecord(record)
        })

        /**
          * the returned Jedis instance has to be the same PARTITION with the gotten
          */
        recordRedisDao.close()
      })
    })

    //userClicks.saveAsTextFiles("hdfs://master:9000/user/root/userClicksDir")
    userClicks.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
