package bonita.kafka.watcher;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
//@EmbeddedKafka(partitions = 1,
//        topics = {AppTest.ORDERS_TOPIC})
public class AppTest {

    static final String ORDERS_TOPIC = "orders";

    @Autowired
    private Receiver receiver;

    @Autowired
    private Sender sender;

    @Test
    public void testReceive() throws Exception {
        sender.send("{\"name\" : \"order placed from java\", \"id\" : \"6969\"}");

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount()).isEqualTo(0);
    }
}