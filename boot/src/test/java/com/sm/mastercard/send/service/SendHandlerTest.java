/**
 *
 */
package com.sm.mastercard.send.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.constants.McSendErrorDescription;
import com.sm.mastercard.send.model.Error;
import com.sm.mastercard.send.model.*;
import com.sm.mastercard.send.util.McSendTestUtil;
import com.sm.mastercard.send.util.McSendUtil;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author arindam.seal
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SendHandlerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendHandlerTest.class);

	@Value("${send.paymentType}")
	private String paymentType;

	@Value("${send.merchantCategoryCode}")
	private String merchantCategoryCode;

    @Mock
    private static PaymentsRequest paymentsRequest;

	@Mock
	private static PaymentsResponse paymentsResponse;

	@Mock
	private static TransferEligibilityRequest transferEligibilityRequest;

	@Mock
	private static McTransferEligibilityResponse transferEligibilityResponse;

    @Mock
    private static McSendUtil mcSendUtil;

    @InjectMocks
    private static SendHandler sendHandler = new SendHandler();

    @InjectMocks
    private McSendTestUtil mcSendTestUtil = new McSendTestUtil();

    private static String participantId;
    private static String partnerId;
    private static String correlationId;
    private static String signature;

    @BeforeClass
    public static void setUp() {
    	paymentsRequest = Mockito.mock(PaymentsRequest.class);
		transferEligibilityRequest = Mockito.mock(TransferEligibilityRequest.class);
		transferEligibilityResponse = Mockito.mock(McTransferEligibilityResponse.class);
		paymentsResponse = Mockito.mock(PaymentsResponse.class);
        participantId = McSendTestUtil.getParticipantId();
        partnerId = McSendTestUtil.getPartnerId();
        correlationId = McSendTestUtil.getCorrelationId();
        signature = McSendTestUtil.getSignature();
        mcSendUtil = Mockito.mock(McSendUtil.class);
		ReflectionTestUtils.setField(sendHandler, "paymentType", "P2P");
		ReflectionTestUtils.setField(sendHandler, "merchantCategoryCode", "6536");
    }

	//----------------------- Generic -------------------------//Start
	@Test
	public void test_customSignatureCreationResponse() {

		LOGGER.info("Came into method test_customSignatureCreationResponse() .. ");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("X-Correlation-Id", correlationId);
		httpHeaders.set("X-JWS-Signature", signature);
		ResponseEntity<?> signatureRes = new ResponseEntity<Object>( httpHeaders , HttpStatus.OK);

		Message message = MessageBuilder.withPayload(signatureRes).build();

		//expected
		Message response = MessageBuilder.withPayload(new PaymentsResponse())
				.setHeader("X-Correlation-Id",signatureRes.getHeaders().get("X-Correlation-Id").get(0))
				.setHeader("X-JWS-Signature",signatureRes.getHeaders().get("X-JWS-Signature").get(0))
				.setHeader(org.springframework.integration.http.HttpHeaders.STATUS_CODE, HttpStatus.OK)
				.build();

		MessageBuilder builder = MessageBuilder.fromMessage(response);

		Message actualMessage = sendHandler.customSignatureCreationResponse(message);

		Assertions.assertThat(actualMessage.getHeaders().containsKey("X-Correlation-Id"))
				.isSameAs(response.getHeaders().containsKey("X-Correlation-Id"));

		LOGGER.info("Exiting method test_customSignatureCreationResponse() .. ");
	}

	@Test
	public void test_generateCustomResponse() {

		LOGGER.info("Came to method test_generateCustomResponse ....");

		String[] payload = {"SocketTimeoutException","signature"};
		List<Object> payloads =  Arrays.asList(payload);

		McErrorResponse errorResponse = new McErrorResponse();
		Errors errors = new Errors();
		Error err = new Error();
		List<Error> errorList = new ArrayList<Error>();

		err.setReasonCode(McSendErrorCodes.INTERNAL_SECURITY_TIMEOUT_ERROR);
		err.setDescription(McSendErrorDescription.INTERNAL_SECURITY_TIMEOUT_ERROR);
		err.setSource("Timeout error");
		err.setRecoverable(false);

		errorList.add(err);
		errors.setError(errorList);
		errorResponse.setErrors(errors);

		Message errMessage = MessageBuilder.withPayload(errorResponse)
				.build();

		Message<?> actualMessage =  sendHandler.generateCustomResponse(payloads.toString());

		Assertions.assertThat(actualMessage.getPayload())
				.isEqualToComparingFieldByFieldRecursively(errMessage.getPayload());

		LOGGER.info("Exiting method test_generateCustomResponse ....");

	}

	//----------------------- Generic -------------------------//End

	//----------------------- MC Payments -------------------------//Start
    @Test
    public void test_generateToken_payments() throws URISyntaxException, IOException{

    	LOGGER.info("Came to method test_generateToken ...");

    	 //Building input request
		URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getOiToSmRequest_payments());
		File requestFile = Paths.get(request.toURI()).toFile();
		paymentsRequest = McSendTestUtil.objectMapper().readValue(requestFile, PaymentsRequest.class);

		boolean enableRepeatFlag = true; boolean enableEncryption = true;

		ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(paymentsRequest);

		String paymentsUrl = "http://localhost:25043/send/partners/{partner_id}/payments?repeat_flag={repeatFlag}";

		String url = paymentsUrl.replace("{partner_id}", partnerId)
                .replace("{repeatFlag}", String.valueOf(enableRepeatFlag));

		String authToken = "058716";

		Mockito.lenient().when(mcSendUtil
				.generateAuthTokenForMcSend(url, payload))
			.thenReturn(authToken);

		Message message = MessageBuilder.withPayload(paymentsRequest)
                .setHeader("X-Encrypted",enableEncryption)
                .setHeader("X-Partner-Id",partnerId)
                .setHeader("X-Repeat-Flag",enableRepeatFlag)
                .setHeader("Authorization",authToken)
                .setHeader("Content-Type", "application/json")
                .build();

    	Message actualMessage =  sendHandler.generateToken(
    			paymentsRequest, partnerId, enableRepeatFlag, enableEncryption, paymentsUrl);

    	Assertions.assertThat(actualMessage.getPayload())
    		.isEqualToComparingFieldByFieldRecursively(message.getPayload());


    	LOGGER.info("Exiting method test_generateToken ....");
    }

    @Test
	public void test_signatureVerificationPayments(){
		LOGGER.info("Came to method test_signatureVerificationPayments ...");
		Message<?> message = MessageBuilder.withPayload(paymentsRequest)
				.setHeader(McSendConstants.CORRELATION_ID,correlationId)
				.setHeader(McSendConstants.X_JWS_SIGNATURE,signature)
				.setHeader(McSendConstants.X_PARTICIPANT_ID,participantId)
				.setHeader("Content-Type", "application/json")
				.build();

		Message actualMessage =  sendHandler.signatureVerificationPayments(
				paymentsRequest, correlationId, signature, participantId);

		Assertions.assertThat(actualMessage.getPayload())
				.isEqualToComparingFieldByFieldRecursively(message.getPayload());

		LOGGER.info("Exiting method test_signatureVerificationPayments ....");
	}

	@Test
	public void test_signatureGenerationPayments() throws URISyntaxException, IOException{
		LOGGER.info("Came to method test_signatureGenerationPayments ...");

		//Building output response
		URL response = SendHandlerTest.class.getResource(mcSendTestUtil.getSmToOiResponse_payments());
		File responseFile = Paths.get(response.toURI()).toFile();
		paymentsResponse = McSendTestUtil.objectMapper().readValue(responseFile, PaymentsResponse.class);


		Message<?> message = MessageBuilder.withPayload(paymentsResponse)
				.setHeader(McSendConstants.CORRELATION_ID,correlationId)
				.setHeader("Content-Type", "application/json")
				.build();

		Message actualMessage =  sendHandler.signatureGenerationPayments(
				paymentsResponse, correlationId);

		Assertions.assertThat(actualMessage.getPayload())
				.isEqualToComparingFieldByFieldRecursively(message.getPayload());

		LOGGER.info("Exiting method test_signatureGenerationPayments ....");
	}

	//----------------------- MC Payments -------------------------//End

	//----------------------- Transfer Eligibility ----------------------------//Start
	@Test
	public void test_generateToken_transferEligibility() throws URISyntaxException, IOException {

		LOGGER.info("Came to method test_generateToken_transferEligibility ...");

		//Building input request
		URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getOiToSmRequest_te());
		File requestFile = Paths.get(request.toURI()).toFile();
		transferEligibilityRequest = McSendTestUtil.objectMapper().readValue(requestFile,TransferEligibilityRequest.class);

		boolean enableRepeatFlag = true; boolean enableEncryption = true;

		ObjectMapper mapper = new ObjectMapper();
		String payload = mapper.writeValueAsString(paymentsRequest);

		String paymentsUrl = "http://localhost:25043/send/partners/{partner_id}/transfer-eligibility";

		String url = paymentsUrl.replace("{partner_id}", partnerId);

		String authToken = "058716";

		Mockito.lenient().when(mcSendUtil
				.generateAuthTokenForMcSend(url, payload))
				.thenReturn(authToken);

		Message message = MessageBuilder.withPayload(transferEligibilityRequest)
				.setHeader("X-Encrypted",enableEncryption)
				.setHeader("Authorization",authToken)
				.setHeader("Content-Type", "application/json")
				.build();

		Message actualMessage =  sendHandler.generateTokenForEligibility(
				transferEligibilityRequest, partnerId, enableEncryption, paymentsUrl);

		Assertions.assertThat(actualMessage.getPayload())
				.isEqualToComparingFieldByFieldRecursively(message.getPayload());


		LOGGER.info("Exiting method test_generateToken ....");
	}

	@Test
	public void test_signatureVerificationTransferEligibility(){
		LOGGER.info("Came to method test_signatureVerificationTransferEligibility ...");
		Message<?> message = MessageBuilder.withPayload(transferEligibilityRequest)
				.setHeader(McSendConstants.CORRELATION_ID,correlationId)
				.setHeader(McSendConstants.X_JWS_SIGNATURE,signature)
				.setHeader(McSendConstants.X_PARTICIPANT_ID,participantId)
				.setHeader("Content-Type", "application/json")
				.build();

		Message actualMessage =  sendHandler.signatureVerificationTransferEligibility(
				transferEligibilityRequest, correlationId, signature, participantId);

		Assertions.assertThat(actualMessage.getPayload())
				.isEqualToComparingFieldByFieldRecursively(message.getPayload());

		LOGGER.info("Exiting method test_signatureVerificationTransferEligibility ....");
	}

	@Test
	public void test_signatureGenerationTransferEligibility() throws URISyntaxException, IOException{
		LOGGER.info("Came to method test_signatureGenerationTransferEligibility ...");

		//Building output response
		URL response = SendHandlerTest.class.getResource(mcSendTestUtil.getSmToOiResponse_payments());
		File responseFile = Paths.get(response.toURI()).toFile();
		transferEligibilityResponse = McSendTestUtil.objectMapper().readValue(responseFile, McTransferEligibilityResponse.class);

		Message<?> message = MessageBuilder.withPayload(transferEligibilityResponse)
				.setHeader(McSendConstants.CORRELATION_ID,correlationId)
				.setHeader(McSendConstants.X_JWS_SIGNATURE,signature)
				.setHeader(McSendConstants.X_PARTICIPANT_ID,participantId)
				.setHeader("Content-Type", "application/json")
				.build();

		Message actualMessage =  sendHandler.signatureGenerationTransferEligibility(
				transferEligibilityResponse, correlationId);

		Assertions.assertThat(actualMessage.getPayload())
				.isEqualToComparingFieldByFieldRecursively(message.getPayload());

		LOGGER.info("Exiting method test_signatureGenerationTransferEligibility ....");
	}

	//----------------------- Transfer Eligibility ----------------------------//End

    @AfterClass
    public static void tearDown() {

        System.out.println(SendHandlerTest.class+" has been tested and code coverage is checked .. ");
    }
}
