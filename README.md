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

## How To Use

### Prerequisites
* [Apache Kafka](https://kafka.apache.org/)
* [Bonita](https://www.bonitasoft.com/) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bonitasoft.engine/bonita-server/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bonitasoft.engine/bonita-server)
* [NodeJS](https://nodejs.org/en/)
* [Java 8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Build
```
$ cd bonita-kafka-watcher
$ ./gradlew build
$ cd ../order
$ yarn install
$ cd ../payment
$ yarn install
$ cd ../shipment
$ yarn install
```

### Run

#### bonita-kafka-watcher
**Prerequisites**: Kafka and Bonita servers must be running and accessible. Server URLs can be set in the [application.yml](https://github.com/nchabanoles/microservices-orchestration-talk/raw/master/bonita-kafka-watcher/src/main/resources/application.yml) configuration file.
```
$ cd bonita-kafka-watcher
$ ./gradlew run
```

#### Payment and Shipment services
**Prerequisites**: 
* Kafka server is running and reachable
* order service is stopped

From the service folder:
```
$ yarn start
```

#### Order service
**Note**:  Each time the order service is started it produces a New Order event and publish it to Kafka. Other services will consume the message (only if they are already running)
```
$ cd order
$ yarn start
```

## Demo

To create a reproducible demo environment you can use the Dockerfile descriptors to build Docker images.
```
$ cd order
$ sudo docker build --no-cache -t nodejs-orders-service:v1 .
$ cd ../payment
$ sudo docker build --no-cache -t nodejs-payments-service:v1 .
$ cd ../shipment
$ sudo docker build --no-cache -t nodejs-shipments-service:v1 .
$ cd ../bonita-kafka-watcher
$ sudo docker build -f Dockerfile-Monitoring --no-cache -t bonita-choreography:v1 .
$ sudo docker build -f Dockerfile-PaymentService --no-cache -t bonita-payments-service-proxy:v1 .
```

**Note**: the bonita-kafka-watcher project is used twice to create 2 distinct docker images. The first one for the Demo 2 and second one for demo 3.

### Demo 1 (no Bonita involved)
**Prerequisites**: Kafka is running and reachable (I started a Docker container locally).

I started a Docker containers locally using [Portainer](https://www.portainer.io/installation/).

* Start Shipment service (nodejs-shipments-service container)
* Start Payment service (nodejs-payments-service container)
* Start Order service (nodejs-orders-service container) ==> an order is automatically issued and processed by Payment service then Shipment service.

Restart the Order service (i.e the Docker container itself) as often as you want to produce a 'New Order Available' event.

### Demo 2 (Bonita as an alternate implementation of Payment service)
For the sake of the completeness of how Bonita can be used in an Event-Driven Microservices Architecture I demonstrated that a microservice can be easily implemented with a BPMN process using Bonita.

**Prerequisites**: Bonita server is running and the Payment service process implementation has been installed. See [microservice-orchestration-bonita-order-process-example](https://github.com/nchabanoles/microservice-orchestration-bonita-order-process-example).

After Demo 1 has been played:
* Stop nodejs-payments-service container
* Start bonita-payments-service-proxy container (it only forwards 'New Order Available' messages to Bonita)
* Open Bonita admin console to the see the process execution
* Re-start nodejs-orders-service container to produce a 'New Order Available' message (and check in Bonita that a new instance of the payment process has been executed)

![Payment Service implemented with Bonita](https://github.com/nchabanoles/microservices-orchestration-talk/raw/master/pics/payment-process-microservice.png)


### Demo 3 (Bonita as Choreograph)

**Prerequisites**: Bonita server is running and the Order Management process has been installed. See [microservice-orchestration-bonita-order-process-example](https://github.com/nchabanoles/microservice-orchestration-bonita-order-process-example).

After Demo 2 has been played:
* Start the bonita-choreography container
* Open Bonita admin console to the see the process execution
* Re-start nodejs-orders-service container to produce a 'New Order Available' message (and check in Bonita that a new instance of the payment process has been executed)


![Choreography implemented with Bonita](https://github.com/nchabanoles/microservices-orchestration-talk/raw/master/pics/bonita-microservices-choreography.png)
