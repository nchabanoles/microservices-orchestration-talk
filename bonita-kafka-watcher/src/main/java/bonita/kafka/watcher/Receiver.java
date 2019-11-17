package bonita.kafka.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Receiver {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Receiver.class);

    @Value("${bonita.server.url}")
    private String bonitaServerURL;

    @Value("${bonita.server.username}")
    private String username;

    @Value("${bonita.server.password}")
    private String password;

    @Value("${bonita.target-process.name}")
    private String targetProcessName;

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }




    @KafkaListener(topics = "#{'${kafka.consumer.topics}'.split(',')}")
    public void receive(String payload) {

        LOGGER.info("received payload='{}'", payload);
        latch.countDown();
        // call bonita to send a BPM Event with payload content
        new BPMMessagePublisher(bonitaServerURL, targetProcessName, username, password).sendBPMMessageFromPayload(payload);
    }


}
