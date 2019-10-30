import com.cxlsky.UserApplication;
import com.cxlsky.mq.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class TestApplication {

    @Autowired
    private MessageProcessor messageProcessor;

    @Test
    public void message() {
        messageProcessor.output("hahahahahahaha");
    }
}
