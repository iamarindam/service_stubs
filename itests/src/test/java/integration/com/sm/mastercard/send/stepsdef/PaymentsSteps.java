package integration.com.sm.mastercard.send.stepsdef;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.model.PaymentsResponse;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import integration.com.sm.mastercard.send.dataobject.PaymentsRequest;
import integration.com.sm.mastercard.send.util.RunnerUtil;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Step Definition Class for Payments.
 *
 */

public class PaymentsSteps extends RunnerUtil {
    private static final ZappLogger log = ZappLoggerFactory.getLogger(PaymentsSteps.class, true);

    private String signature;
    @Given("user wants to make a payment request with the payment reference {string}")
    public void giveData(String paymentReference) {
        testContext().reset();
        PaymentsRequest paymentsRequest = new PaymentsRequest();
        try {
            URL request = PaymentsSteps.class.getResource("/templates/PaymentsRequest.json");
            File requestFile = Paths.get(request.toURI()).toFile();
            paymentsRequest = objectMapper().readValue(requestFile, PaymentsRequest.class);
        }catch (Exception ex){
            log.warn("Exception occurred while parsing json",ex);
        }
        paymentsRequest.setPaymentReference(paymentReference);
        testContext().setPayload(paymentsRequest);
    }

    @When("user generates payments signature")
    public void generateToken(){
        executePost(configFileReader.getPaymentsSigUrl());
        Response response = testContext().getResponse();
        signature = response.getBody().asString();
    }

    @And("user sends payments post request")
    public void postRequest(){
        Map<String,Object> headers = setHeaders(signature);
        executePostWithHeaders(configFileReader.getPaymentsUrl(),headers);
    }

    @And("verify payments response is valid")
    public void validateSuccessResponse(){
        Response response = testContext().getResponse();
        PaymentsResponse actualResponse = response.as(PaymentsResponse.class);

        PaymentsResponse expectedResponse = null;
        try {
            URL request = PaymentsSteps.class.getResource("/templates/PaymentsResponse.json");
            File requestFile = Paths.get(request.toURI()).toFile();
            expectedResponse = objectMapper().readValue(requestFile, PaymentsResponse.class);
        }catch (Exception ex){
            log.warn("Exception occurred while parsing json",ex);
        }
        Assertions.assertThat(actualResponse)
                .isEqualToComparingFieldByFieldRecursively(expectedResponse);
    }
}
