package integration.com.sm.mastercard;

import com.sm.mastercard.send.McSendApplication;
import cucumber.api.java.Before;
import integration.com.sm.mastercard.send.config.ApplicationTestContext;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = McSendApplication.class)
public class SendApplicationTests {
  private static final Logger LOG = LoggerFactory.getLogger(SendApplicationTests.class);

  @Test
  public void contextLoads() {
  }

  @Before
  public void initCucumber() {
    LOG.info("------------------Cucumber Initialized Successfully--------------------");
  }

  @After
  public void tearDown() {
    LOG.info("------------- TEST CONTEXT TEAR DOWN -------------");
    ApplicationTestContext.CONTEXT.reset();
  }
}