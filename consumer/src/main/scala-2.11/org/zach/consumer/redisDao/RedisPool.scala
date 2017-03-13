package org.zach.consumer.redisDao

import com.typesafe.config.ConfigFactory
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{Jedis, JedisPool}

/**
  * Create a Singleton object: RedisPool
  * the Singleton object will be initialized until it is first visited
  */
object RedisPool {

  val config = ConfigFactory.load()

  val redisHost = config.getString("redis.host")
  val redisPort = config.getInt("redis.port")
  val redisDB = config.getString("redis.database.name")
  val dbIndex = config.getInt("redis.database.index")

  val maxTotal = config.getInt("redis.pool.maxTotal")
  val maxIdle = config.getInt("redis.pool.maxIdle")
  val minIdle = config.getInt("redis.pool.minIdle")
  val testOnBorrow = config.getBoolean("redis.pool.testOnBorrow")
  val testOnReturn = config.getBoolean("redis.pool.testOnReturn")

  val poolConfig = new GenericObjectPoolConfig
  poolConfig.setMaxTotal(maxTotal)
  poolConfig.setMaxIdle(maxIdle)
  poolConfig.setMinIdle(minIdle)
  poolConfig.setTestOnBorrow(testOnBorrow)
  poolConfig.setTestOnReturn(testOnReturn)

  val redisTimeOut = config.getInt("redis.timeOut")

  val redisPool = new JedisPool(
    poolConfig,
    redisHost, redisPort,
    redisTimeOut,
    null,
    dbIndex
  )

  def getResource(): Jedis = {
    redisPool.getResource
  }

  def returnResource(): Unit = {
    redisPool.close()
  }

  val hook = new Thread {
    override def run = {
      /**
        * only when the App runs in the local mode,
        * can we see the printed result.Besides, the number of the
        * printed result due to the local thread.
        */
      println("Execute hook thread: " + this)
      redisPool.destroy()
    }
  }

  /**
    * Be sure that the JedisPool is destroyed, after the App stops
    */
  sys.addShutdownHook(hook.run)

  def main(args: Array[String]): Unit = {
    println(getResource())
  }
}
