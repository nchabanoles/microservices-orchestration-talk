package bonita.kafka.watcher;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BPMMessagePublisherTest {

    @Test
    public void testSend() throws Exception {
        boolean messageSent = new BPMMessagePublisher().sendBPMMessageFromPayload("{\"name\" : \"order placed from java\", \"id\" : \"6969\"}");

        assertThat(messageSent).isTrue();
    }
}
