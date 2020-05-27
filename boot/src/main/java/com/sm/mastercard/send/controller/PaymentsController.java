package com.sm.mastercard.send.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendEnum;
import com.sm.mastercard.send.model.PaymentsRequest;
import com.sm.mastercard.send.service.PaymentsService;
import com.sm.mastercard.send.util.McSendUtil;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(McSendConstants.URL)
@Validated
public class PaymentsController implements PaymentsControllerApi{
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(PaymentsController.class,true);

    private HttpServletRequest request;

    @Autowired
    private PaymentsService paymentsService;

    public PaymentsController() {}

    @Autowired
    public PaymentsController(HttpServletRequest request) {
        this.request = request;
    }

    @PostMapping(value = McSendConstants.PAYMENTS_URL, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> payments(
            @ApiParam(value = McSendConstants.SEND_PARTICIPANT_PARAM)
            @RequestHeader(value=McSendConstants.X_PARTICIPANT_ID,required=true)
            @Size(max= 35, message = McSendConstants.SEND_PARTICIPANT_SIZE)  String participantId

            ,@ApiParam(value = McSendConstants.SEND_BMIDENT_PARAM)
            @RequestHeader(value=McSendConstants.X_BUSINESS_MESSAGE_IDENTIFIER,required=true)
            @Size(max= 40, message = McSendConstants.SEND_BMIDENT_SIZE) String businessMsgIdentifier

            ,@ApiParam(value = McSendConstants.SEND_SIGNATURE_PARAM)
            @RequestHeader(value=McSendConstants.X_JWS_SIGNATURE,required=true) String signature

            ,@ApiParam(value = McSendConstants.SEND_REPEAT_PARAM)
            @RequestHeader(value = McSendConstants.X_REPEAT_FLAG, required=false)  boolean repeat

            ,@ApiParam(value = McSendConstants.SEND_ENCRYPTED_PARAM)
            @RequestHeader(value=McSendConstants.X_ENCRYPTED, required=false) Boolean encrypted

            ,@ApiParam @Valid @RequestBody PaymentsRequest paymentsRequest
    ) throws IOException {
        LOG.info(McSendEnum.PAYMENTS_START.getLog());

        String correlationId = McSendUtil.generateCorrelationId(request);
        LOG.debug(McSendEnum.PAYMENTS_CORR_ID.getLog() , correlationId);
        try {
            return  paymentsService.initiatePayments(paymentsRequest,correlationId,participantId,businessMsgIdentifier,
                    signature,repeat,encrypted);
        }finally{
            LOG.info(McSendEnum.PAYMENTS_CONTROLLER_END.getLog() , correlationId);
        }
    }

    @PostMapping(value = McSendConstants.PAYMENTS_URL+"-test/generate/token", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<String> generateJwsToken(@RequestBody PaymentsRequest paymentsRequest){
        LOG.info(McSendEnum.PAYMENTS_SIGNATURE_GEN.getLog());
        return paymentsService.generateJwsToken(paymentsRequest);
    }
}
