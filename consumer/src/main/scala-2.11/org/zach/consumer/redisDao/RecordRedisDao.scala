package org.zach.consumer.redisDao

import com.typesafe.config.ConfigFactory
import org.zach.consumer.model.Record

class RecordRedisDao {

  val redisClient = RedisPool.getResource()

  val config = ConfigFactory.load()
  val dbHashKey = config.getString("redis.database.hashKey")

  /** value=value+clickCount where hash=myHashKey and key=uid
    * if uid do not exist,create uid
    * if value do not exist,value=0 and value=value+clickCount */
  def insertRecord(record: Record) = {
    redisClient.hincrBy(dbHashKey, record.field, record.value)
  }

  def close() = {
    redisClient.close()
  }

}
