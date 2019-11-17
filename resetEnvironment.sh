#!/bin/bash

## Change to desired working directory (i.e. Kafka install folder)
cd ../kafka_2.12-2.3.0/

## Make sure everything is stopped
bin/kafka-server-stop.sh
sleep 3
bin/zookeeper-server-stop.sh
sleep 3


## Clean temporary folders and empty kafka content (existing messages and topics)
rm -rf /tmp/zookeeper/
rm -rf /tmp/kafka-logs/

## Start servers
bin/zookeeper-server-start.sh config/zookeeper.properties &
sleep 3
bin/kafka-server-start.sh config/server.properties &
sleep 5

## Create necessary topics
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic payments
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic shipments
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ordersconsistency

echo ''
echo '-----------------------------------------------------------------------------------'
echo '--  Environment clean of messages, you can start services and place order now.  ---'
echo '-----------------------------------------------------------------------------------'
