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
import com.sm.mastercard.send.model.McTransferEligibilityResponse;
import com.sm.mastercard.send.model.TransferEligibilityRequest;
import com.sm.mastercard.send.util.McSendUtil;

@Service
public class TransferEligibilityService {
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(TransferEligibilityService.class,true);

    @Autowired
    private McSendIntegrationGateway mcSendIntegrationGateway;

    @Autowired
    private McSendUtil mcSendUtil;

    @Value("${send.enable.signing}")
    private boolean isSigningEnabled;

    @Value("${send.validation.source}")
    private String source;

    @Value("${send.partnerId}")
    private String partnerId;

    public ResponseEntity<Object> initiateTransferEligibility(TransferEligibilityRequest transferEligibilityRequest,
      String correlationId, String participantId, String businessMessageIdentifier,String signature,boolean isEncrypted) throws IOException{
        LOG.debug(McSendEnum.TE_SIGNING_ENABLED.getLog() , isSigningEnabled);
        if(isSigningEnabled) {
            //call signing service for the verification of request signature form OI -> SM
            Message<?> signatureVerificationResponse = verifyTransferEligibilityRequest(transferEligibilityRequest,
                    correlationId,signature,participantId);
            HttpStatus statusCode = (HttpStatus) signatureVerificationResponse.getHeaders().get(McSendConstants.HTTP_STATUSCODE);
            if(HttpStatus.OK!=statusCode){
                LOG.debug(McSendEnum.SIGNATURE_VER_FAILED.getLog());
                return mcSendUtil.generateErrorResponseToAHI(statusCode,signatureVerificationResponse);
            }
            LOG.debug(McSendEnum.SIGNATURE_VER_SUCCESS.getLog());
        }

        return processTransferEligibility(transferEligibilityRequest,correlationId,
                participantId,businessMessageIdentifier,isEncrypted,signature);
    }

    private Message verifyTransferEligibilityRequest(TransferEligibilityRequest request, String correlationId,
                                                       String signature, String participantId) {
        Message<?> signatureVerificationRequest = MessageBuilder.withPayload(request)
                .setHeader(McSendConstants.CORRELATION_ID, correlationId)
                .setHeader(McSendConstants.X_JWS_SIGNATURE, signature)
                .setHeader(McSendConstants.X_PARTICIPANT_ID, participantId)
                .build();
        return mcSendIntegrationGateway.transferEligibilitySignatureVerification(signatureVerificationRequest);
    }

    private ResponseEntity<Object> processTransferEligibility(TransferEligibilityRequest transferEligibilityRequest,
                                                             String correlationId,String xParticipantId,String businessMsgId,boolean xEncrypted,String signature) throws IOException{
        Message<?> transferEligibilityResponse =  mcSendIntegrationGateway.
                eligibility(transferEligibilityRequest,correlationId,xParticipantId,partnerId,
                        xEncrypted);
        HttpStatus statusCode = (HttpStatus)transferEligibilityResponse.getHeaders().
                get(McSendConstants.HTTP_STATUSCODE);

        if(HttpStatus.OK != statusCode && HttpStatus.CREATED != statusCode){
            LOG.debug(McSendEnum.TE_ERROR_RESPONSE.getLog());
            return mcSendUtil.generateErrorResponseToAHI(statusCode,transferEligibilityResponse);
        }else{
            LOG.debug(McSendEnum.TE_SUCCESS_RESPONSE.getLog());
            McTransferEligibilityResponse response = (McTransferEligibilityResponse) transferEligibilityResponse.getPayload();
            return processTransferEligibilityResponse(statusCode,response,correlationId,
                    businessMsgId,signature);
        }
    }

    private Message generateResponseSignatureForTransferEligibility(McTransferEligibilityResponse response, String correlationId) {
        Message<?> signatureGenerationRequest = MessageBuilder.withPayload(response)
                .setHeader(McSendConstants.CORRELATION_ID, correlationId)
                .build();
        return mcSendIntegrationGateway.transferEligibilitySigningGeneration(signatureGenerationRequest);
    }

    private ResponseEntity<Object> processTransferEligibilityResponse(HttpStatus statusCode,McTransferEligibilityResponse response,
                                                                      String correlationId,String businessMessageIdentifier,String signature) throws IOException{

        LOG.debug(McSendEnum.TE_RESPONSE_MSG.getLog());
        HttpHeaders headers = new HttpHeaders();
        
        String format = "^[a-zA-Z0-9]+$";
        
        if (!businessMessageIdentifier.matches(format))
            throw new IOException();
        
        headers.set(McSendConstants.X_BUSINESS_MESSAGE_IDENTIFIER,businessMessageIdentifier);
        if(isSigningEnabled) {
            Message<?> signatureGenerationResponse = generateResponseSignatureForTransferEligibility(response,correlationId);
            statusCode = (HttpStatus) signatureGenerationResponse.getHeaders().get(McSendConstants.HTTP_STATUSCODE);
            if(HttpStatus.OK != statusCode){
                LOG.debug(McSendEnum.SIGNATURE_GEN_FAILED.getLog());
                return mcSendUtil.generateErrorResponseToAHI(statusCode,signatureGenerationResponse);
            }
            LOG.info(McSendEnum.SIGNATURE_GEN_SUCCESS.getLog());
            signature = signatureGenerationResponse.getHeaders().get(McSendConstants.X_JWS_SIGNATURE).toString();
        }

        if (!signature.matches(format))
            throw new IOException();
        
        headers.set(McSendConstants.X_JWS_SIGNATURE,signature);
        LOG.debug(McSendEnum.SIGNATURE_VER_SUCCESS.getLog());
        return new ResponseEntity<>(response,headers,statusCode);
    }

    public ResponseEntity<String> generateJwsToken(TransferEligibilityRequest transferEligibilityRequest){
        Message<?> signatureGenRes = generateJwsTokenForTransferEligibility(transferEligibilityRequest);
        HttpStatus statusCode = (HttpStatus) signatureGenRes.getHeaders().get(McSendConstants.HTTP_STATUSCODE);
        if(HttpStatus.OK == statusCode){
            String signature = signatureGenRes.getHeaders().get(McSendConstants.X_JWS_SIGNATURE).toString();
            LOG.debug(McSendEnum.SIGNATURE_GEN_SUCCESS.getLog());
            return new ResponseEntity<>(signature,HttpStatus.OK);
        }
        LOG.debug(McSendEnum.SIGNATURE_GEN_FAILED.getLog());
        return new ResponseEntity<>("Error while generating signature",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Message generateJwsTokenForTransferEligibility(TransferEligibilityRequest transferEligibilityRequest){
        Message<?> signatureGenRequest = org.springframework.integration.support.MessageBuilder.
                withPayload(transferEligibilityRequest)
                .setHeader(McSendConstants.CORRELATION_ID, UUID.randomUUID().toString())
                .build();
        return mcSendIntegrationGateway.testSignatureGeneration(signatureGenRequest);
    }
}
