const kafka = require('kafka-node');
const bp = require('body-parser');
const config = require('./config');

const Producer = kafka.Producer;
const client = new kafka.KafkaClient({"kafkaHost":config.kafka_server});
const producer = new Producer(client);
const kafka_topic = config.kafka_topic;

const orderID = '041981';

let payloads = [
  {
    topic: kafka_topic,
    messages: `{"name": "New Order Available", "id": "${orderID}" }`
  }
];

producer.on('ready', async function() {
  console.log(`Order Service ready, pushing payloads to Kafka server: ${client.options.kafkaHost}`);
  producer.send(payloads, (err, data) => {
    if (err) {
      console.log(`[Order Service -> ${kafka_topic}]: broker update failed`, err);
    } else {
      console.log(`[Order Service -> ${kafka_topic}]: new order successfuly placed: ${orderID}`);
    }
  });
});

producer.on('error', function(err) {
  console.log(`[Order Service -> ${kafka_topic}]: connection error, terminating program.`, err);
});
