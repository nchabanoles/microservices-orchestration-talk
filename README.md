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

