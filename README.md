# microservices-orchestration-talk

## Setup Kafka

### Download kafka
https://www.apache.org/dyn/closer.cgi?path=/kafka/2.3.0/kafka_2.12-2.3.0.tgz

### Untar

### Start servers
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

### Create a Shipment Topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic shipment


## Troubleshoot

	- listing topics
		bin/kafka-topics.sh --list --bootstrap-server localhost:9092

	- Produce some message
		bin/kafka-console-producer.sh --broker-list localhost:9092 --topic shipment
	- Consume some messages
		bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic shipment --from-beginning

--------------------------------------------------------
-- Micro services communicating using events on Kafka --
--------------------------------------------------------

Order Service (based on NodeJS and Kafka-node):
	> nvm use stable
	> npm install
	> npm run start
