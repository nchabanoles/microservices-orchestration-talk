# microservices-orchestration-talk

## Setup Kafka

### Download kafka
https://www.apache.org/dyn/closer.cgi?path=/kafka/2.3.0/kafka_2.12-2.3.0.tgz

### Untar

### Start servers
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

### Create a Orders Topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders


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

The easiest way is to execute the resetEnvironment.sh script that will start Zookeeper and Kafka servers + create required topics.
Then once invited by the script, execute the npm run start of each service.


Order Service (based on NodeJS and Kafka-node):
	> nvm use stable
	> npm install
	> npm run start
--> Published an event on the orders topic with order id in a JSON message

Payment Service (based on NodeJS and Kafka-node):
	> nvm use stable
	> npm install
	> npm run start
--> Listen from orders topic for new orders
--> Publish a message on payments topic to notify paiement received for a specific order id

