package com.sm.mastercard.send.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendEnum;
import com.sm.mastercard.send.integration.config.McSendIntegrationGateway;
import com.sm.mastercard.send.model.PaymentsRequest;
import com.sm.mastercard.send.model.PaymentsResponse;
import com.sm.mastercard.send.util.McSendUtil;

@Service
public class PaymentsService {
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(PaymentsService.class,true);

    @Autowired
    private McSendIntegrationGateway mcSendIntegrationGateway;

    @Autowired
    private McSendUtil mcSendUtil;

    @Value("${send.partnerId}")
    private String partnerId;

    @Value("${send.enable.signing}")
    private boolean isSigningEnabled;

    public ResponseEntity<Object> initiatePayments(PaymentsRequest paymentsRequest,String correlationId,
                                                   String participantId,String businessMsgIdentifier,
                                                   String signature,boolean repeat,boolean encrypted) throws IOException {
        LOG.debug(McSendEnum.PAYMENTS_SIGNING_ENABLED.getLog() ,isSigningEnabled);
        if (isSigningEnabled) {
            //call signing service for the verification of request signature form OI -> SM
            Message<?> signatureVerificationResponse = verifyPaymentsRequest(paymentsRequest, correlationId, signature,
                    participantId);
            HttpStatus statusCode = (HttpStatus) signatureVerificationResponse.getHeaders().get(McSendConstants.HTTP_STATUSCODE);
            if (HttpStatus.OK != statusCode) {
                LOG.debug(McSendEnum.SIGNATURE_VER_FAILED.getLog());
                return mcSendUtil.generateErrorResponseToAHI(statusCode, signatureVerificationResponse);
            }
            LOG.debug(McSendEnum.SIGNATURE_VER_SUCCESS.getLog());
        }
        return processPayments(paymentsRequest,correlationId, participantId, businessMsgIdentifier,
                signature, repeat, encrypted);
    }

    private Message verifyPaymentsRequest(PaymentsRequest request, String correlationId,
                                             String signature, String participantId) {
        Message<?> signatureVerificationRequest = MessageBuilder.withPayload(request)
                .setHeader(McSendConstants.CORRELATION_ID,correlationId)
                .setHeader(McSendConstants.X_JWS_SIGNATURE,signature)
                .setHeader(McSendConstants.X_PARTICIPANT_ID,participantId)
                .build();
        return  mcSendIntegrationGateway.
                signatureVerification(signatureVerificationRequest);
    }

    private ResponseEntity<Object> processPayments(PaymentsRequest paymentsRequest,String correlationId,
                                                   String participantId,String businessMsgId, String signature,boolean repeat ,boolean encrypted) throws IOException{
        Message<?> paymentsResponse = mcSendIntegrationGateway.send(paymentsRequest,correlationId,
                partnerId,participantId,encrypted,repeat);
        HttpStatus statusCode = (HttpStatus)paymentsResponse.getHeaders().get(McSendConstants.HTTP_STATUSCODE);

        if(HttpStatus.OK != statusCode && HttpStatus.CREATED != statusCode){
            LOG.debug(McSendEnum.PAYMENTS_ERROR_RESPONSE.getLog());
            return mcSendUtil.generateErrorResponseToAHI(statusCode,paymentsResponse);
        }else{
            LOG.debug(McSendEnum.PAYMENTS_SUCCESS_RESPONSE.getLog());
            PaymentsResponse response = (PaymentsResponse) paymentsResponse.getPayload();
            return processPaymentsResponse(statusCode,response,correlationId,participantId,
                    businessMsgId,signature);
        }
    }

    private ResponseEntity<Object> processPaymentsResponse(HttpStatus  statusCode, PaymentsResponse paymentsResponse,
                           String correlationId,String participantId, String businessMsgIdentifier,String signature) throws IOException{
        LOG.debug(McSendEnum.PAYMENTS_RESPONSE_MSG.getLog());
        HttpHeaders headers = new HttpHeaders();
        
        String format = "^[a-zA-Z0-9]+$";
        
        if (!participantId.matches(format))
            throw new IOException();
        
        headers.set(McSendConstants.X_PARTICIPANT_ID,participantId);
        
        if (!businessMsgIdentifier.matches(format))
            throw new IOException();
        
        headers.set(McSendConstants.X_BUSINESS_MESSAGE_IDENTIFIER,businessMsgIdentifier);

        if(isSigningEnabled) {
            Message<?> signatureGenerationResponse = generateResponseSignatureForPayments(paymentsResponse,correlationId);
            statusCode = (HttpStatus) signatureGenerationResponse.getHeaders().get(McSendConstants.HTTP_STATUSCODE);
            if(HttpStatus.OK != statusCode){
                LOG.debug(McSendEnum.SIGNATURE_GEN_FAILED.getLog());
                return mcSendUtil.generateErrorResponseToAHI(statusCode,signatureGenerationResponse);
            }
            LOG.debug(McSendEnum.SIGNATURE_VER_SUCCESS.getLog());
            signature = signatureGenerationResponse.getHeaders().get(McSendConstants.X_JWS_SIGNATURE).toString();
        }
        
        
        if (!signature.matches(format))
            throw new IOException();
        
        headers.set(McSendConstants.X_JWS_SIGNATURE,signature);
        return new ResponseEntity<>(paymentsResponse,headers,statusCode);
    }

    private Message generateResponseSignatureForPayments(PaymentsResponse response, String correlationId) {
        Message<?> signatureGenerationRequest = MessageBuilder.withPayload(response)
                .setHeader(McSendConstants.CORRELATION_ID,correlationId)
                .build();
        return  mcSendIntegrationGateway.signingGeneration(signatureGenerationRequest);
    }

    public ResponseEntity<String> generateJwsToken(PaymentsRequest paymentsRequest){
        Message<?> signatureGenRes = generateJwsTokenForPayments(paymentsRequest);
        HttpStatus statusCode = (HttpStatus) signatureGenRes.getHeaders().get(McSendConstants.HTTP_STATUSCODE);
        if(HttpStatus.OK == statusCode){
            String signature = signatureGenRes.getHeaders().get(McSendConstants.X_JWS_SIGNATURE).toString();
            LOG.debug(McSendEnum.SIGNATURE_GEN_SUCCESS.getLog());
            return new ResponseEntity<>(signature,HttpStatus.OK);
        }
        LOG.debug(McSendEnum.SIGNATURE_GEN_FAILED.getLog());
        return new ResponseEntity<>("Error while generating signature",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Message generateJwsTokenForPayments(PaymentsRequest paymentsRequest){
        Message<?> signatureGenRequest = MessageBuilder.withPayload(paymentsRequest)
                .setHeader(McSendConstants.CORRELATION_ID, UUID.randomUUID().toString())
                .build();
        return mcSendIntegrationGateway.testSignatureGeneration(signatureGenRequest);
    }
}


