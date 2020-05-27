package com.sm.mastercard.send.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.integration.config.McSendIntegrationGateway;
import com.sm.mastercard.send.model.McTransferEligibilityResponse;
import com.sm.mastercard.send.model.PaymentsRequest;
import com.sm.mastercard.send.model.PaymentsResponse;
import com.sm.mastercard.send.model.SigningServiceResponse;
import com.sm.mastercard.send.model.TransferEligibilityRequest;
import com.sm.mastercard.send.service.SendHandler;

@Configuration
@IntegrationComponentScan
@EnableIntegration
public class McSendIntegrationConfig {
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(McSendIntegrationConfig.class,true);

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

    @Autowired
    private McSendIntegrationGateway mcSendIntegrationGateway;

    @Bean
    public SendHandler sendHandler(){
        return new SendHandler();
    }

    //----------------------- Generic -------------------------//Start
    @Bean
    public HttpComponentsClientHttpRequestFactory getClientHttpRequestFactoryForPayments() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeOutInMilliSec);
        clientHttpRequestFactory.setReadTimeout(timeOutInMilliSec);
        return clientHttpRequestFactory;
    }

    @Bean
    public IntegrationFlow handleRecovery() {
        return IntegrationFlows.from("integration.send.error.channel")
                .channel("error.transformer.input")
                .get();
    }

    @Transformer(inputChannel = "error.transformer.input")
    public Message transformer(Message<?> message) {
        Message<?> msg;

        if(message.getPayload().toString().contains("ResourceAccessException"))
            msg = mcSendIntegrationGateway.handleTimeoutErrorFlow(message);
        else
            msg = mcSendIntegrationGateway.handleClientErrorFlow(message);
        return msg;
    }

    @Bean
    public IntegrationFlow handleTimeoutErrorFlow() {
        return IntegrationFlows.from("integration.send.error.timeoutError.channel")
                .transform(Throwable::getCause)
                .<ResourceAccessException>handle((p, h) ->
                        MessageBuilder.withPayload(sendHandler().generateCustomResponse(p.getLocalizedMessage())
                                .getPayload())
                                .setHeader(HttpHeaders.STATUS_CODE,HttpStatus.GATEWAY_TIMEOUT)
                                .build())
                .get();
    }

    @Bean
    public IntegrationFlow handleClientErrorFlow() {
        return IntegrationFlows.from("integration.send.error.clientError.channel")
                .transform(Throwable::getCause)
                .<HttpStatusCodeException>handle((p, h) ->
                        MessageBuilder.withPayload(sendHandler().generateCustomResponse(p.getResponseBodyAsString())
                                .getPayload())
                                .setHeader(HttpHeaders.STATUS_CODE, p.getStatusCode())
                                .build())
                .get();
    }

    @Bean
    public IntegrationFlow testSignatureGeneration(){
        return IntegrationFlows.from("integration.signing.generate.test.signature")
                .enrichHeaders(h -> h.header("Content-Type", "application/json"))
                .handle(Http.outboundGateway(testSignatureGenerationURL)
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("*").extractPayload(true)
                        .requestFactory(getClientHttpRequestFactoryForPayments()))
                .transform(sendHandler())
                .get();
    }
    //----------------------- Generic -------------------------//End

    //----------------------- MC Payments -------------------------//Start
    @Bean
    public IntegrationFlow signatureVerification(){
        return IntegrationFlows.from("integration.signature.verification.channel")
                .handle((p,h) -> sendHandler().signatureVerificationPayments((PaymentsRequest) p,
                        (String)h.get(McSendConstants.CORRELATION_ID),
                        (String)h.get(McSendConstants.X_JWS_SIGNATURE),
                        (String)h.get(McSendConstants.X_PARTICIPANT_ID)))
                .handle(Http.outboundGateway(signatureVerificationURL)
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("*").extractPayload(true)
                        .requestFactory(getClientHttpRequestFactoryForPayments())
                        .expectedResponseType(SigningServiceResponse.class))
                .get();
    }

    @Bean
    public IntegrationFlow signatureGeneration(){
        return IntegrationFlows.from("integration.signature.generation.channel")
                .handle((p,h) -> sendHandler().signatureGenerationPayments((PaymentsResponse) p,
                        (String)h.get(McSendConstants.CORRELATION_ID)))
                .handle(Http.outboundGateway(signatureGenerationURL)
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("*").extractPayload(true)
                        .requestFactory(getClientHttpRequestFactoryForPayments()))
                .transform(sendHandler())
                .get();
    }

    @Bean
    public IntegrationFlow mcSendPayment(){
        return IntegrationFlows.from("integration.send.request.channel")
                .handle((p,h) -> sendHandler().generateToken((PaymentsRequest)p,
                        (String)h.get("X-Partner-Id"),
                        (boolean)h.get("X-Repeat-Flag"),(boolean)h.get("X-Encrypted"),paymentsUrl))
                .handle(Http.outboundGateway(paymentsUrl)
                        .uriVariable("partner_id", h -> h.getHeaders().get("X-Partner-Id"))
                        .uriVariable("repeatFlag", h -> h.getHeaders().get("X-Repeat-Flag"))
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("*")
                        .extractPayload(true)
                        .requestFactory(getClientHttpRequestFactoryForPayments())
                        .expectedResponseType(PaymentsResponse.class))
                .get();
    }
    //----------------------- MC Payments -------------------------// End

    //----------------------- Transfer Eligibility ----------------------------//Start
    @Bean
    public IntegrationFlow transferEligibilitySignatureVerification(){
        return IntegrationFlows.from("integration.transfer-eligibility.signature.verification.channel")
                .handle((p,h) -> sendHandler().signatureVerificationTransferEligibility((TransferEligibilityRequest) p,
                        (String)h.get(McSendConstants.CORRELATION_ID),
                        (String)h.get(McSendConstants.X_JWS_SIGNATURE),
                        (String)h.get(McSendConstants.X_PARTICIPANT_ID)))
                .handle(Http.outboundGateway(signatureVerificationURL)
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("*").extractPayload(true)
                        .requestFactory(getClientHttpRequestFactoryForPayments())
                        .expectedResponseType(SigningServiceResponse.class))
                .get();
    }

    @Bean
    public IntegrationFlow transferEligibilitySignatureGeneration(){
        return IntegrationFlows.from("integration.transfer-eligibility.signature.generation.channel")
                .handle((p,h) -> sendHandler().signatureGenerationTransferEligibility((McTransferEligibilityResponse) p,
                        (String)h.get(McSendConstants.CORRELATION_ID)))
                .handle(Http.outboundGateway(signatureGenerationURL)
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("*").extractPayload(true)
                        .requestFactory(getClientHttpRequestFactoryForPayments()))
                .transform(sendHandler())
                .get();
    }

    @Bean
    public IntegrationFlow transferEligibility(){
        return IntegrationFlows.from("integration.transfer-eligibility.request.channel")
                .handle((p,h) -> sendHandler().generateTokenForEligibility((TransferEligibilityRequest)p,
                        (String)h.get(McSendConstants.X_PARTNER_ID),
                        (boolean)h.get(McSendConstants.X_ENCRYPTED),
                        transferEligibilityUrl))
                .handle(Http.outboundGateway(transferEligibilityUrl)
                        .uriVariable("partner_id", h -> h.getHeaders().get(McSendConstants.X_PARTNER_ID))
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("*")
                        .extractPayload(true)
                        .requestFactory(getClientHttpRequestFactoryForPayments())
                        .expectedResponseType(McTransferEligibilityResponse.class))
                .get();
    }
    //----------------------- Transfer Eligibility ----------------------------//End
}
