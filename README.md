# Microservices in an Event-Driven Architecture using Bonita and Apache Kafka

This repository contains the material used for my talk about Event-Driven Architecture for Microservices. I demonstrate how Bonita (a BPMs) can be a game changer in such an architecture in term of robusteness, maintanability, evolutivity and monitoring.

## Overview

As examples here are 4 sub-projects implementing an e-commerce application.

* order:  
    A sample NodeJS implementation of an Order service. This service simulates a user placing a new order to buy some goods. 
* payment:  
    A sample NodeJS implementation of a Payment service. This service simulates a system charging customers credit card.
* shipment:  
    A sample NodeJS implementation of a Shipment service. This service simulates a robotic system packing goods to be sent via post mail.

There is also a pre-built adapter hiding the Apache Kafka listening logic to communicate with a Bonita application (BPMN process based app). The implementation of the Bonita application can be found in the [microservice-orchestration-bonita-order-process-example](https://github.com/nchabanoles/microservice-orchestration-bonita-order-process-example) GitHub repository.
![Event-Driven Architecture with Bonita](https://github.com/nchabanoles/microservices-orchestration-talk/raw/master/pics/Event-Driven_Microservices_Bonita-e-commerce.png)

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
    - KAFKA_ADVERTISED_HOST_NAME : kafka
    - KAFKA_ZOOKEEPER_CONNECT : zookeeper:2181
- make sure to expose all ports
- select network orders-microservices

_*Attention:*_ 'kafka' is a dns that can be resolved by other docker containers in the orders-microservices network. If you want to contact kafka from the host, make sure to add an entry in your /etc/hosts to point to the IP address assigned to the container, e.g. 172.23.0.3

### Start containers
Kafka and Zookeeper start automatically when their container starts.

*Note:* If you had to do it manually, here is an example of how to do so:

	bin/zookeeper-server-start.sh config/zookeeper.properties
	bin/kafka-server-start.sh config/server.properties

### Create orders,payments,shipments Topics
Kafka container will create topics automatically when a producer publish a record. 
Nevertheless, it is better to create the topics needed up-front, it avoids listeners (consumers) to face error messages 
until the first record is sent. To do so, add the following System properties to the container configuration:

    KAFKA_CREATE_TOPICS : orders:1:1,payments:1:1,shipments:1:1,ordersconsistency:1:1

*Note:* if you had to create them manually (on a local env for instance) here is an example of how to do it:

	bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders
	bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic payments
	bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic shipments


## Troubleshoot
Connect to kafka container then go to /opt/kafka/bin.  

	- list topics:
		bin/kafka-topics.sh --list --bootstrap-server localhost:9092

	- Produce some message:
		bin/kafka-console-producer.sh --broker-list localhost:9092 --topic shipments

	- Consume some messages:
		bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic shipments --from-beginning


--------------------------------------------------------
Micro services communicating using events on Kafka

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

