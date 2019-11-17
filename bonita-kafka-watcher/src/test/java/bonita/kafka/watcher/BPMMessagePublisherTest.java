package bonita.kafka.watcher;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BPMMessagePublisherTest {

    @Test
    public void testSend() throws Exception {
        boolean messageSent = new BPMMessagePublisher("http://localhost:8080", "KafkaClient", "walter.bates", "bpm").sendBPMMessageFromPayload("{\"name\" : \"PublishRecord\", \"id\" : \"6969\"}");

        assertThat(messageSent).isTrue();
    }
}
