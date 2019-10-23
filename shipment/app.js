const kafka = require('kafka-node');
const bp = require('body-parser');
const config = require('./config');


/**
** Producer setup
**/
const Producer = kafka.Producer;
const kafka_topic = config.kafka_topic;
const producer = new Producer(new kafka.KafkaClient(config.kafka_server));

producer.on('ready', async function() {
    /**
    ** Consumer setup
    **/
      const Consumer = kafka.Consumer;
      const kafka_topic_subscription = config.kafka_topic_subscription;
      let consumer = new Consumer(
        new kafka.KafkaClient(config.kafka_server),
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
        console.log(`[${kafka_topic_subscription} -> Shipment Service]: Need to send goods after order payment: ${message.value}`);
        const orderID = JSON.parse(message.value).id;

         let payloads = [
           {
             topic: kafka_topic,
             messages: `{"name": "Shipment sent", "id": "${orderID}" }`
           }
         ];

         // Process shipment

         producer.send(payloads, (err, data) => {
              if (err) {
                console.log(`[Shipment Service -> ${kafka_topic}]: broker update failed`);
              } else {
                console.log(`[Shipment Service -> ${kafka_topic}]: Shipment sent for order: ${orderID}`);
              }
         });


    })
    consumer.on('error', function(err) {
      console.log('error', err);
    });
});

producer.on('error', function(err) {
   console.log(`[Shipment Service -> ${kafka_topic}]: connection error, terminating program.`, err);
});