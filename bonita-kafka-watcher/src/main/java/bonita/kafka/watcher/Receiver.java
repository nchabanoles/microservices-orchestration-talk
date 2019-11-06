package bonita.kafka.watcher;

import com.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.engine.util.APITypeManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Receiver {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }




    @KafkaListener(topics = "#{'${kafka.consumer.topics}'.split(',')}")
    public void receive(String payload) {

        LOGGER.info("received payload='{}'", payload);
        latch.countDown();
        // call bonita to send a BPM Event with payload content
        new BPMMessagePublisher().sendBPMMessageFromPayload(payload);
    }


}
