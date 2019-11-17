const kafka = require('kafka-node');
const bp = require('body-parser');
const config = require('./config');


/**
** Producer setup
**/
const Producer = kafka.Producer;
const client = new kafka.KafkaClient({"kafkaHost":config.kafka_server});
const producer = new Producer(client);
const kafka_topic = config.kafka_topic;

producer.on('ready', async function() {
console.log(`Payments Service ready, pushing payloads to Kafka server: ${client.options.kafkaHost}`);

    /**
    ** Consumer setup
    **/
      const Consumer = kafka.Consumer;
      const kafka_topic_subscription = config.kafka_topic_subscription;
      let consumer = new Consumer(
        new kafka.KafkaClient({"kafkaHost":config.kafka_server}),
        [{ topic: kafka_topic_subscription, partition: 0 }],
        {
          autoCommit: true,
          fetchMaxWaitMs: 1000,
          fetchMaxBytes: 1024 * 1024,
          encoding: 'utf8',
          fromOffset: 'latest'
        }
      );

    consumer.on('message', async function(message) {
        console.log(`[${kafka_topic_subscription} -> Payment Service]: Requiring payment for : ${message.value}`);
        const orderID = JSON.parse(message.value).id;

         let payloads = [
           {
             topic: kafka_topic,
             messages: `{"name": "Payment received for order", "id": "${orderID}" }`
           }
         ];

         // Process payment
         console.log(`[Payment Service]: Processing customer payment for : ${message.value}...`);
         setTimeout(function(){
            console.log(`[Payment Service]: ... still processing  payment for : ${message.value}...`);
         },5000)
         setTimeout(function(){
            producer.send(payloads, (err, data) => {
                  if (err) {
                    console.log(`[Payment Service -> ${kafka_topic}]: broker update failed`);
                  } else {
                    console.log(`[Payment Service -> ${kafka_topic}]: Payment received for order: ${orderID}`);
                  }
             });
         },10000)

    })
    consumer.on('error', function(err) {
      console.log('error', err);
    });
});

producer.on('error', function(err) {
   console.log(`[Payment Service -> ${kafka_topic}]: connection error, terminating program.`, err);
});