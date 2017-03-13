Analyse user behavior in Kafka and Spark Streaming
============
    1.producer module pushes user data to Kafka Brokers;
    2.spark streaming pull user data from Kafka Brokers by Kafka API;
    3.store results into redis

Prerequisites
============
    1.spark version is 2.1.0 that requres scala version is 2.11.x
    
FAQ
========
    1.don't find net.sf.json-lib maven repo, so need to manually add 
    the jar to lib

