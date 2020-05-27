package integration.com.sm.mastercard.send;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * To run cucumber test
 */
@RunWith(Cucumber.class)
@CucumberOptions(features ={"src/test/resources/features"},
        glue = {"integration.com.sm.mastercard.send.stepsdef","integration.com.sm.mastercard.send.config"},
         plugin = {"pretty", "html:target/cucumber"}

        )
public class IntegrationTestRunner {
}
