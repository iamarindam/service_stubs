package com.sm.mastercard.send.integration.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sm.mastercard.send.config.McSendIntegrationConfig;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.model.PaymentsRequest;
import com.sm.mastercard.send.model.PaymentsResponse;
import com.sm.mastercard.send.service.SendHandler;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class McSendIntegrationConfigTest {
    private static final Logger LOG = LoggerFactory.getLogger(McSendIntegrationConfigTest.class);

    private static String signature;

    @InjectMocks
    private McSendTestUtil mcSendTestUtil = new McSendTestUtil();

    @InjectMocks
    private static McSendIntegrationConfig config = new McSendIntegrationConfig();

    @Mock
    private McSendIntegrationGateway integrationGateway;

    @Mock
    private static SendHandler sendHandler;

    @Value( "${send.timeout}" )
    private int timeOutInMilliSec;

    @Value("${send.payments.url}")
    private String paymentsUrl;

    @Value("${transfer.eligibility.url}")
    private String transferEligibilityUrl;

    @Value("${send.signing.verification.path}" )
    private String signatureVerificationURL;

    @Value("${send.signing.generation.path}" )
    private String signatureGenerationURL;

    @Value("${send.test.signing.generation.path}" )
    private String testSignatureGenerationURL;

    @BeforeClass
    public static void setUp() {
        signature = McSendTestUtil.getSignature();
        ReflectionTestUtils.setField(config, "signatureVerificationURL",
                "http://snv-sprintst3.test.vocalink.co.uk:8082/zapp/security/jose/signature-verifications");

        ReflectionTestUtils.setField(config, "signatureGenerationURL",
                "http://snv-sprintst3.test.vocalink.co.uk:8082/zapp/security/jose/signature-generations");

        ReflectionTestUtils.setField(config, "testSignatureGenerationURL",
                "http://snv-sprintst3.test.vocalink.co.uk:8082/zapp-test/security/jose/signature-generations");

        ReflectionTestUtils.setField(config, "timeOutInMilliSec", 5000);

        ReflectionTestUtils.setField(config, "paymentsUrl",
                "http://apap502m:25043/send/partners/{partner_id}/payments?repeat_flag={repeatFlag}");

        ReflectionTestUtils.setField(config, "transferEligibilityUrl",
                "http://apap502m:25043/send/partners/{partner_id}/transfer-eligibility");
    }


    @Test
    public void test_Handler() {
        sendHandler = config.sendHandler();
        assertNotNull(sendHandler);
    }

    @Test
    public void test_handleClientErrorFlow() {
        LOG.info("inside test_handleClientErrorFlow method ... ");
        IntegrationFlow actualResp = config.handleClientErrorFlow();
        assertNotNull(actualResp);
        LOG.info("Exiting test_handleClientErrorFlow method ... ");
    }

    @Test
    public void test_handleTimeoutErrorFlow() {
        LOG.info("inside test_handleTimeoutErrorFlow method ... ");
        IntegrationFlow actualResp = config.handleTimeoutErrorFlow();
        assertNotNull(actualResp);
        LOG.info("Exiting test_handleTimeoutErrorFlow method ... ");
    }

    @Test
    public void test_signatureVerification() {
        LOG.info("inside test_signatureVerification method ... ");
        IntegrationFlow actualResp = config.signatureVerification();
        assertNotNull(actualResp);
        LOG.info("Exiting test_signatureVerification method ... ");
    }

    @Test
    public void test_signatureGeneration() {
        LOG.info("inside test_signatureGeneration method ... ");
        IntegrationFlow actualResp = config.signatureGeneration();
        assertNotNull(actualResp);
        LOG.info("Exiting test_signatureGeneration method ... ");
    }

    @Test
    public void test_mcSendPayment() {
        LOG.info("inside test_mcSendPayment method ... ");
        IntegrationFlow actualResp = config.mcSendPayment();
        assertNotNull(actualResp);
        LOG.info("Exiting test_mcSendPayment method ... ");
    }

    @Test
    public void test_transferEligibilitySignatureVerification() {
        LOG.info("inside test_transferEligibilitySignatureVerification method ... ");
        IntegrationFlow actualResp = config.transferEligibilitySignatureVerification();
        assertNotNull(actualResp);
        LOG.info("Exiting test_transferEligibilitySignatureVerification method ... ");
    }

    @Test
    public void test_transferEligibilitySignatureGeneration() {
        LOG.info("inside test_transferEligibilitySignatureGeneration method ... ");
        IntegrationFlow actualResp = config.transferEligibilitySignatureGeneration();
        assertNotNull(actualResp);
        LOG.info("Exiting test_transferEligibilitySignatureGeneration method ... ");
    }

    @Test
    public void test_transferEligibility() {
        LOG.info("inside test_transferEligibility method ... ");
        IntegrationFlow actualResp = config.transferEligibility();
        assertNotNull(actualResp);
        LOG.info("Exiting test_transferEligibility method ... ");
    }

    @Test
    public void test_testSignatureGeneration() {
        LOG.info("inside testSignatureGeneration method ... ");
        IntegrationFlow actualResp = config.testSignatureGeneration();
        assertNotNull(actualResp);
        LOG.info("Exiting testSignatureGeneration method ... ");
    }

    @Test
    public void test_handleRecovery() {
        LOG.info("inside test_handleRecovery method ... ");
        IntegrationFlow actualResp = config.handleRecovery();
        assertNotNull(actualResp);
        LOG.info("Exiting test_handleRecovery method ... ");
    }

    @Test
    public void test_transformer()
            throws URISyntaxException, JsonParseException, JsonMappingException, IOException, ServletException {

        LOG.info("inside test_transformer method ... ");

        ResourceAccessException ex = new ResourceAccessException("Exception");
        Message<?> msg2 = MessageBuilder.withPayload(ex)
                .setHeader(McSendConstants.X_PARTICIPANT_ID, "123").build();

        MessageBuilder builder2 = MessageBuilder.fromMessage(msg2);
        when(integrationGateway.handleTimeoutErrorFlow(builder2.build())).thenReturn(builder2.build());
        Message<?> res = config.transformer(builder2.build());

        Assertions.assertThat(res.getPayload()).isEqualToComparingFieldByFieldRecursively(ex);

        LOG.info("Exiting test_transformer method ... ");

    }

    @Test
    public void test_transformerFailure()
            throws URISyntaxException, JsonParseException, JsonMappingException, IOException, ServletException {

        LOG.info("inside test_transformerFailure method ... ");

        RuntimeException ex = new RuntimeException("Exception");
        Message<?> msg2 = MessageBuilder.withPayload(ex)
                .setHeader(McSendConstants.X_PARTICIPANT_ID, "123").build();

        MessageBuilder builder2 = MessageBuilder.fromMessage(msg2);

        when(integrationGateway.handleClientErrorFlow(builder2.build())).thenReturn(builder2.build());
        Message<?> res = config.transformer(builder2.build());

        Assertions.assertThat(res.getPayload()).isEqualToComparingFieldByFieldRecursively(ex);

        LOG.info("Exiting test_transformerFailure method ... ");

    }


    @AfterClass
    public static void tearDown() {
        System.out.println(McSendIntegrationConfigTest.class + " has been tested and code coverage is checked .. ");
    }
}
