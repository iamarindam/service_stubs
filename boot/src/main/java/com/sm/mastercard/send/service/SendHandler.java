package com.sm.mastercard.send.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.constants.McSendErrorDescription;
import com.sm.mastercard.send.model.Error;
import com.sm.mastercard.send.model.*;
import com.sm.mastercard.send.util.McSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * @Author: Apeksha Ubhayakar
 * @Date: 17-12-2019
 *
 */


@Component
public class SendHandler{
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(SendHandler.class,false);

    @Value("${send.paymentType}")
    private String paymentType;

    @Value("${send.merchantCategoryCode}")
    private String merchantCategoryCode;

    @Autowired
    private McSendUtil mcSendUtil;

    //----------------------- Generic -------------------------//Start
    @Transformer
    public Message customSignatureCreationResponse(Message<?> message){
        ResponseEntity<?> signatureRes = (ResponseEntity) message.getPayload();
        return MessageBuilder.withPayload(new PaymentsResponse())
                .setHeader(McSendConstants.CORRELATION_ID,signatureRes.getHeaders().get(McSendConstants.CORRELATION_ID).get(0))
                .setHeader(McSendConstants.X_JWS_SIGNATURE,signatureRes.getHeaders().get(McSendConstants.X_JWS_SIGNATURE).get(0))
                .setHeader(HttpHeaders.STATUS_CODE, HttpStatus.OK)
                .build();
    }

    @Transformer
    public Message generateCustomResponse(String payload){
        try{
            McErrorResponse mcErrorResponse = new McErrorResponse();
            if (payload.contains("SocketTimeoutException")) {
                Errors errors = new Errors();
                Error err = new Error();
                List<Error> errorList = new ArrayList<>();

                err.setReasonCode(McSendErrorCodes.INTERNAL_TIMEOUT_ERROR);
                err.setDescription(McSendErrorDescription.INTERNAL_TIMEOUT_ERROR);
                if(payload.contains("signature")){
                    err.setReasonCode(McSendErrorCodes.INTERNAL_SECURITY_TIMEOUT_ERROR);
                    err.setDescription(McSendErrorDescription.INTERNAL_SECURITY_TIMEOUT_ERROR);
                }
                err.setSource("Timeout error");
                err.setRecoverable(false);
                errorList.add(err);
                errors.setError(errorList);
                mcErrorResponse.setErrors(errors);
            }else{
                mcErrorResponse = new ObjectMapper().readValue(payload,
                        McErrorResponse.class);
            }
            return MessageBuilder.withPayload(mcErrorResponse)
                    .build();

        }catch(Exception ex){
            LOG.error("Exception at errorResponseChannel :",ex);
            throw new RuntimeException(ex);
        }
    }
    //----------------------- Generic -------------------------//End

    //----------------------- MC Payments -------------------------//Start
    @Transformer
    public Message generateToken(PaymentsRequest request,String partnerId,
                                    boolean enableRepeatFlag,boolean enableEncryption,
                                    String paymentsUrl){
        try {
            request.setMerchantCategoryCode(merchantCategoryCode);
            request.setPaymentType(paymentType);

            String payload = new ObjectMapper().writeValueAsString(request);
            String url = paymentsUrl.replace("{partner_id}", partnerId)
                    .replace("{repeatFlag}", String.valueOf(enableRepeatFlag));

            String authToken = mcSendUtil.generateAuthTokenForMcSend(url, payload);
            return MessageBuilder.withPayload(request)
                    .setHeader(McSendConstants.X_ENCRYPTED,enableEncryption)
                    .setHeader(McSendConstants.X_PARTNER_ID,partnerId)
                    .setHeader(McSendConstants.X_REPEAT_FLAG,enableRepeatFlag)
                    .setHeader(McSendConstants.AUTHORIZATION,authToken)
                    .setHeader(McSendConstants.CONTENT_TYPE, McSendConstants.APPLICATION_JSON).build();
        }catch (Exception ex){
            LOG.warn("Exception in MC-Send Payments service :", ex);
            throw new RuntimeException(ex);
        }
    }

    @Transformer
    public Message signatureVerificationPayments(PaymentsRequest request,
                                                    String correlationId,String signature,String participantId){
        return MessageBuilder.withPayload(request)
                .setHeader(McSendConstants.CORRELATION_ID,correlationId)
                .setHeader(McSendConstants.X_JWS_SIGNATURE,signature)
                .setHeader(McSendConstants.X_PARTICIPANT_ID,participantId)
                .setHeader(McSendConstants.CONTENT_TYPE, McSendConstants.APPLICATION_JSON)
                .build();
    }

    @Transformer
    public Message signatureGenerationPayments(PaymentsResponse response,
                                                  String correlationId){
        return MessageBuilder.withPayload(response)
                .setHeader(McSendConstants.CORRELATION_ID,correlationId)
                .setHeader(McSendConstants.CONTENT_TYPE, McSendConstants.APPLICATION_JSON)
                .build();
    }
    //----------------------- MC Payments -------------------------//End

    //----------------------- Transfer Eligibility ----------------------------//Start
    @Transformer
    public Message signatureVerificationTransferEligibility(TransferEligibilityRequest request,
                                                               String correlationId,String signature,String participantId){
        return MessageBuilder.withPayload(request)
                .setHeader(McSendConstants.CORRELATION_ID,correlationId)
                .setHeader(McSendConstants.X_JWS_SIGNATURE,signature)
                .setHeader(McSendConstants.X_PARTICIPANT_ID,participantId)
                .setHeader(McSendConstants.CONTENT_TYPE, McSendConstants.APPLICATION_JSON)
                .build();
    }

    @Transformer
    public Message signatureGenerationTransferEligibility(McTransferEligibilityResponse response,
                                                             String correlationId){
        return MessageBuilder.withPayload(response)
                .setHeader(McSendConstants.CORRELATION_ID,correlationId)
                .setHeader(McSendConstants.CONTENT_TYPE, McSendConstants.APPLICATION_JSON)
                .build();
    }

    @Transformer
    public Message generateTokenForEligibility(TransferEligibilityRequest request,
                                                  String partnerId,boolean isEncrypted,String transferEligibilityUrl) {
        try {
            request.setPaymentType(paymentType);
            String payload = new ObjectMapper().writeValueAsString(request);

            String url = transferEligibilityUrl.replace("{partner_id}", partnerId);
            String authToken = mcSendUtil.generateAuthTokenForMcSend(url, payload);

            return MessageBuilder.withPayload(request)
                    .setHeader(McSendConstants.X_ENCRYPTED, isEncrypted)
                    .setHeader(McSendConstants.AUTHORIZATION, authToken)
                    .setHeader(McSendConstants.CONTENT_TYPE, McSendConstants.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            LOG.error("Exception in generateTokenForEligibility :" , ex);
            throw new RuntimeException(ex);
        }
    }
    //----------------------- Transfer Eligibility ----------------------------//End
}


