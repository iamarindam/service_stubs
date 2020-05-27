package com.sm.mastercard.send.controller;

import com.sm.mastercard.send.model.PaymentsRequest;
import com.sm.mastercard.send.model.PaymentsResponse;
import com.sm.mastercard.send.service.PaymentsService;
import com.sm.mastercard.send.service.SendHandlerTest;
import com.sm.mastercard.send.util.McSendTestUtil;
import org.assertj.core.api.Assertions;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.isA;

/*
 *
 * @Author: Apeksha Ubhayakar
 * @Date: 21-11-2019
 *
 */

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations="classpath:application.properties")
public class McSendControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(McSendControllerTest.class);

    private static String participantId;
    private static String businessMsgIdentifier;
    private static String signature;

    @InjectMocks
    private McSendTestUtil mcSendTestUtil = new McSendTestUtil();

    @Mock
    private static PaymentsService paymentsService;

    @InjectMocks
    private static PaymentsController mcSendController = new PaymentsController();

    @Mock
    private static PaymentsRequest paymentsRequest;

    @Mock
    private static PaymentsResponse paymentsResponse;

    @BeforeClass
    public static void setUp() {
        participantId = McSendTestUtil.getParticipantId();
        businessMsgIdentifier = McSendTestUtil.getBusinessMsgIdentifier();
        signature=McSendTestUtil.getSignature();
        paymentsRequest = Mockito.mock(PaymentsRequest.class);
        paymentsResponse = Mockito.mock(PaymentsResponse.class);
        paymentsService = Mockito.mock(PaymentsService.class);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
    public void test_mcSend() throws IOException, URISyntaxException {

        LOGGER.info("test_mcSend() test method execution started..");

        boolean repeat = false , encrypted = false;

        //Building input request
		URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getOiToSmRequest_payments());
		File requestFile = Paths.get(request.toURI()).toFile();
		paymentsRequest = McSendTestUtil.objectMapper().readValue(requestFile, PaymentsRequest.class);

		//Building output response
		URL response = SendHandlerTest.class.getResource(mcSendTestUtil.getSmToOiResponse_payments());
		File responseFile = Paths.get(response.toURI()).toFile();
		paymentsResponse = McSendTestUtil.objectMapper().readValue(responseFile, PaymentsResponse.class);

        ResponseEntity<Object> message = new ResponseEntity<>(paymentsResponse,HttpStatus.CREATED);

		//expected
		PaymentsResponse sendResponse = (PaymentsResponse)message.getBody();

		//when
		Mockito.when(paymentsService.initiatePayments(
                isA(PaymentsRequest.class),//paymentsRequest,
                isA(String.class),//correlationId,
                isA(String.class),//participantId,
                isA(String.class),//businessMsgId,
                isA(String.class),//signature,
                isA(Boolean.class), //encrypted,
                isA(Boolean.class)) )//repeat))
			.thenReturn(message); //then

		//actual
        ResponseEntity<Object> responseEntity =  mcSendController.payments(
        		participantId, businessMsgIdentifier, signature,repeat,encrypted,paymentsRequest);

        Assertions.assertThat(responseEntity.getBody())
        	.isEqualToComparingFieldByFieldRecursively(sendResponse);

        LOGGER.info("test_mcSend() test method execution finished..");
    }

    @Test
    public void test_generateJwsToken() throws URISyntaxException, IOException {
    	LOGGER.info("came into test_generateJwsToken() method ... ");

    	//Building input request
    	URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getOiToSmRequest_payments());
		File requestFile = Paths.get(request.toURI()).toFile();
		paymentsRequest = McSendTestUtil.objectMapper().readValue(requestFile, PaymentsRequest.class);

        ResponseEntity<String> res = new ResponseEntity<>(signature,HttpStatus.OK);

  		Mockito.when(paymentsService.generateJwsToken(isA(PaymentsRequest.class)))
  				.thenReturn(res);

    	ResponseEntity<String> actualMessage = mcSendController.generateJwsToken(paymentsRequest);

    	Assertions.assertThat(actualMessage.getBody())
    		.isSameAs(signature);

    	LOGGER.info("Exiting test_generateJwsToken() method ... ");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println(McSendControllerTest.class+" has been tested and code coverage checked .. ");
    }

}
