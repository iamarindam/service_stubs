package integration.com.sm.mastercard.send.stepsdef;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.model.McTransferEligibilityResponse;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import integration.com.sm.mastercard.send.dataobject.TransferEligibilityTestRequest;
import integration.com.sm.mastercard.send.util.RunnerUtil;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Step Definition Class for Transfer Eligibility.
 *
 */

public class TransferEligibilitySteps extends RunnerUtil {
  private static final ZappLogger log = ZappLoggerFactory.getLogger(TransferEligibilitySteps.class, true);
  private String signature;

  @Given("user wants to check transfer eligibility with the following attributes")
  public void giveData(DataTable dataTable) {
    testContext().reset();
    List<List<TransferEligibilityTestRequest>>  teList = dataTable.asList(TransferEligibilityTestRequest.class);
    testContext().setPayload(teList.get(0));
  }

  @When("user generates transfer eligibility signature")
  public void generateToken(){
    executePost(configFileReader.getTeSigUrl());
    Response response = testContext().getResponse();
    signature = response.getBody().asString();
  }

  @And("user sends transfer eligibility post request")
  public void postRequest(){
    Map<String,Object> headers = setHeaders(signature);
    executePostWithHeaders(configFileReader.getTransferEligibilityUrl(),headers);
  }

  @And("verify transfer eligibility response is valid")
  public void validateSuccessResponse(){
    Response response = testContext().getResponse();
    McTransferEligibilityResponse actualResponse = response.as(McTransferEligibilityResponse.class);

    McTransferEligibilityResponse expectedResponse = null;
    try {
      URL request = PaymentsSteps.class.getResource("/templates/transferEligibilityResponse.json");
      File requestFile = Paths.get(request.toURI()).toFile();
      expectedResponse = objectMapper().readValue(requestFile, McTransferEligibilityResponse.class);
    }catch (Exception ex){
      log.warn("Exception occurred while parsing json",ex);
    }
    Assertions.assertThat(actualResponse)
            .isEqualToComparingFieldByFieldRecursively(expectedResponse);
  }
}
