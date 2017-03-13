Analyse user behavior in Kafka and Spark Streaming
============
    1.producer module pushes user data to Kafka Brokers;
    2.spark streaming pull user data from Kafka Brokers by Kafka API;
    3.store results into redis

Prerequisites
============
    1.spark version is 2.1.0 that requres scala version is 2.11.x
    2.kafka version is 2.11-0.10.2.0
    3.need to start Kafka server and create topic hotSpot
    4.need to start redis server
    
FAQ
========
    1.don't find net.sf.json-lib maven repo, so need to manually add the jar to lib
    
    
    2.redis.clients.jedis.exceptions.JedisConnectionException: Could not get a resource from the pool
    Caused by: redis.clients.jedis.exceptions.JedisConnectionException: 
    java.net.ConnectException: Connection refused
    
    start redis server
    
    
    3.DENIED Redis is running in protected mode because protected mode is enabled, 
    no bind address was specified, no authentication password is requested to clients. 
    In this mode connections are only accepted from the loopback interface. 
    If you want to connect from external computers to Redis you may adopt one of the following solutions: 
    1) Just disable protected mode sending the command 'CONFIG SET protected-mode no' from the loopback interface 
    by connecting to Redis from the same host the server is running, 
    however MAKE SURE Redis is not publicly accessible from internet if you do so. 
    Use CONFIG REWRITE to make this change permanent. 
    2) Alternatively you can just disable the protected mode by editing the Redis configuration file, 
    and setting the protected mode option to 'no', and then restarting the server. 
    3) If you started the server manually just for testing, restart it with the '--protected-mode no' option. 
    4) Setup a bind address or an authentication password. 
    NOTE: You only need to do one of the above things in order for the server to start accepting connections from the outside.

