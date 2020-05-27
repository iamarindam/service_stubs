package com.sm.mastercard.send.controller;

import com.sm.mastercard.send.model.McTransferEligibilityResponse;
import com.sm.mastercard.send.model.TransferEligibilityRequest;
import com.sm.mastercard.send.service.SendHandlerTest;
import com.sm.mastercard.send.service.TransferEligibilityService;
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

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations="classpath:application.properties")
public class TransferEligibilitiesControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(McSendControllerTest.class);

    private static String participantId;
    private static String businessMsgIdentifier;
    private static String signature;

    @InjectMocks
    private McSendTestUtil mcSendTestUtil = new McSendTestUtil();

    @Mock
    private static TransferEligibilityService transferEligibilityService;

    @InjectMocks
    private static TransferEligibilitiesController transferEligibilitiesController = new TransferEligibilitiesController();

    @Mock
    private static TransferEligibilityRequest transferEligibilityRequest;

    @Mock
    private static McTransferEligibilityResponse transferEligibilityResponse;

    @BeforeClass
    public static void setUp() {
        participantId = McSendTestUtil.getParticipantId();
        businessMsgIdentifier = McSendTestUtil.getBusinessMsgIdentifier();
        signature=McSendTestUtil.getSignature();
        transferEligibilityRequest = Mockito.mock(TransferEligibilityRequest.class);
        transferEligibilityResponse = Mockito.mock(McTransferEligibilityResponse.class);
        transferEligibilityService = Mockito.mock(TransferEligibilityService.class);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void test_transferEligibilityService() throws IOException, URISyntaxException {

        LOGGER.info("test_transferEligibilityService() test method execution started..");

        boolean encrypted = false;

        //Building input request
        URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getOiToSmRequest_te());
        File requestFile = Paths.get(request.toURI()).toFile();
        transferEligibilityRequest = McSendTestUtil.objectMapper().readValue(requestFile, TransferEligibilityRequest.class);

        //Building output response
        URL response = SendHandlerTest.class.getResource(mcSendTestUtil.getSmToOiResponse_te());
        File responseFile = Paths.get(response.toURI()).toFile();
        transferEligibilityResponse = McSendTestUtil.objectMapper().readValue(responseFile, McTransferEligibilityResponse.class);

        ResponseEntity<Object> message = new ResponseEntity<>(transferEligibilityResponse,HttpStatus.CREATED);

        //expected
        McTransferEligibilityResponse teResponse = (McTransferEligibilityResponse)message.getBody();

        //when
        Mockito.when(transferEligibilityService.initiateTransferEligibility(
                isA(TransferEligibilityRequest.class),//transferEligibilityRequest,
                isA(String.class),//correlationId,
                isA(String.class),//participantId,
                isA(String.class),//businessMsgId,
                isA(String.class),//partnerId,
                isA(Boolean.class)) )//enableEncryption))
                .thenReturn(message); //then

        //actual
        ResponseEntity<Object> responseEntity =  transferEligibilitiesController.transferEligibilityService(
                participantId, businessMsgIdentifier, signature, transferEligibilityRequest,encrypted);

        Assertions.assertThat(responseEntity.getBody())
                .isEqualToComparingFieldByFieldRecursively(teResponse);

        LOGGER.info("test_transferEligibilityService() test method execution finished..");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void test_generateJwsToken() throws URISyntaxException, IOException {
        LOGGER.info("came into test_generateJwsToken() method ... ");

        //Building input request
        URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getOiToSmRequest_te());
        File requestFile = Paths.get(request.toURI()).toFile();
        transferEligibilityRequest = McSendTestUtil.objectMapper().readValue(requestFile, TransferEligibilityRequest.class);

        ResponseEntity<String> res = new ResponseEntity<>(signature,HttpStatus.OK);

        Mockito.when(transferEligibilityService.generateJwsToken(isA(TransferEligibilityRequest.class)))
                .thenReturn(res);

        ResponseEntity<String> actualMessage = transferEligibilitiesController.
                generateJwsToken(transferEligibilityRequest);

        Assertions.assertThat(actualMessage.getBody())
                .isSameAs(signature);

        LOGGER.info("Exiting test_generateJwsToken() method ... ");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println(TransferEligibilitiesController.class+" has been tested and code coverage checked .. ");
    }
}
