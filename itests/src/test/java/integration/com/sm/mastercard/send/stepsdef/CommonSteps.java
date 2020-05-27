package integration.com.sm.mastercard.send.stepsdef;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.model.ErrorResponse;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import integration.com.sm.mastercard.send.util.RunnerUtil;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


/**
 * Step Definition Class for common steps.
 *
 */

public class CommonSteps  extends RunnerUtil {
    private static final ZappLogger log = ZappLoggerFactory.getLogger(CommonSteps.class, true);

    @Then("the response is {string}")
    public void checkResponse(String expectedResult){
        Response response = testContext().getResponse();
        switch (expectedResult) {
            case "IS SUCCESSFUL":
                assertThat(response.statusCode()).isIn(200, 201);
                break;
            case "FAILS":
                assertThat(response.statusCode()).isBetween(400, 504);
                break;
            default:
                fail("Unexpected error");
        }
    }

    @And("verify error response is valid")
    public void validateFailureResponse(){
        Response response = testContext().getResponse();
        ErrorResponse actualResponse = response.as(ErrorResponse.class);
        ErrorResponse expectedResponse = null;
        try {
            URL request = CommonSteps.class.getResource("/templates/ErrorResponse.json");
            File requestFile = Paths.get(request.toURI()).toFile();
            expectedResponse = objectMapper().readValue(requestFile, ErrorResponse.class);
        }catch (Exception ex){
            log.warn("Exception occurred while parsing json",ex);
        }
        Assertions.assertThat(actualResponse)
                .isEqualToComparingFieldByFieldRecursively(expectedResponse);
    }
}
