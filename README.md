# microservices-orchestration-talk

## Setup Kafka

### From Portainer
Create a dedicated network named orders-microservices

Create a container named zookeeper:
- from wurstmeister/zookeeper:latest (version 3.4.13 at the time of writing)
- make sure to expose all ports
- select network orders-microservices

Create a container named kafka 
- from wurstmeister/kafka:latest (version 2.3.0 at the time of writing)
- manually expose port localhost:9092 toward 9092
- set env variables 
    - KAFKA_ADVERTISED_HOST_NAME : localhost
    - KAFKA_ZOOKEEPER_CONNECT : zookeeper:2181
- make sure to expose all ports
- select network orders-microservices

### Start containers
Kafka and Zookeeper start automatically when their container starts.

*Note:* If you had to do it manually, here is an example of how to do so:
	bin/zookeeper-server-start.sh config/zookeeper.properties
	bin/kafka-server-start.sh config/server.properties

### Create orders,payments,shipments Topics
Kafka container will create topics automatically for you. You do not have to do anything.

*Note:* if you had to create them manually here is an example of how to do it:
	bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders
	bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic payments
	bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic shipments


## Troubleshoot
Connect to kafka container then go to /opt/kafka/bin.  

	- listing topics
		bin/kafka-topics.sh --list --bootstrap-server localhost:9092

	- Produce some message
		bin/kafka-console-producer.sh --broker-list localhost:9092 --topic shipments
	- Consume some messages
		bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic shipments --from-beginning

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

