package com.sm.mastercard.send.integration.config;

import com.sm.mastercard.send.model.PaymentsRequest;
import com.sm.mastercard.send.model.TransferEligibilityRequest;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway(errorChannel = "integration.send.error.channel")
public interface McSendIntegrationGateway {

    //----------------------- Generic ----------------------------//Start

    //Timeout error channel
    @Gateway(requestChannel = "integration.send.error.timeoutError.channel")
    Message handleTimeoutErrorFlow(Message<?> message);

    //MC error channel
    @Gateway(requestChannel = "integration.send.error.clientError.channel")
    Message handleClientErrorFlow(Message<?> message);

    //Signing service ----> test signature generation
    @Gateway(requestChannel = "integration.signing.generate.test.signature")
    Message testSignatureGeneration(Message<?> message);

    //----------------------- Generic ----------------------------//End

    //----------------------- MC Payments ----------------------------//Start

    //Signing service ----> Signature Verification channel
    @Gateway(requestChannel = "integration.signature.verification.channel")
    Message signatureVerification(Message<?> message);

    //Signing service ----> Signature Generation channel
    @Gateway(requestChannel="integration.signature.generation.channel")
    Message signingGeneration(Message<?> message);

    //Mastercard Send service channel
    @Gateway(requestChannel = "integration.send.request.channel")
    Message send(@Payload PaymentsRequest request,
                    @Header("X-Correlation-Id") String correlationId,
                    @Header("X-Partner-Id") String partnerId,
                    @Header("X-Participant-Id") String participantId,
                    @Header("X-Encrypted") boolean enableEncryption,
                    @Header("X-Repeat-Flag") boolean repeatFlag);

    //----------------------- MC Payments ----------------------------//End

    //----------------------- Transfer Eligibility ----------------------------//Start

    //Signing service ----> Signature Verification channel
    @Gateway(requestChannel = "integration.transfer-eligibility.signature.verification.channel")
    Message transferEligibilitySignatureVerification(Message<?> message);


    //Signing service ----> Signature Generation channel
    @Gateway(requestChannel="integration.transfer-eligibility.signature.generation.channel")
    Message transferEligibilitySigningGeneration(Message<?> message);

    //Mastercard Transfer Eligibility channel
    @Gateway(requestChannel = "integration.transfer-eligibility.request.channel")
    Message eligibility(@Payload TransferEligibilityRequest request,
                           @Header("X-Correlation-Id") String correlationId,
                           @Header("X-Participant-Id") String participantId,
                           @Header("X-Partner-Id") String partnerId,
                           @Header("X-Encrypted") boolean enableEncryption);

    //----------------------- Transfer Eligibility ----------------------------//End

}
