package com.sm.mastercard.send.config;

/*
 *
 * @Author: Apeksha Ubhayakar
 * @Date: 03-12-2019
 *
 */

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.constants.McSendEnum;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.constants.McSendErrorDescription;
import com.sm.mastercard.send.model.Error;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ErrorMapper {
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(ErrorMapper.class,true);

    @Value("${send.validation.source}")
    private String source;

    private ConcurrentHashMap<String, Error> errorMap = new ConcurrentHashMap<>();

    public Error getErrorMapper(String key){
        return errorMap.get(key);
    }

    @PostConstruct
    public void loadErrorMapper() {
        LOG.debug(McSendEnum.ERROR_MAPPER_START.getLog());
        Error errorResponse = null;

        //structural error
        errorResponse = generateErrorResponse(McSendErrorCodes.STRUCTURAL_ERROR,
                McSendErrorDescription.STRUCTURAL_ERROR);
        errorMap.put("STVL-9999",errorResponse);

        //Time Out Send
        errorResponse = generateErrorResponse(McSendErrorCodes.TIMEOUT_ERROR,
                McSendErrorDescription.TIMEOUT_ERROR);
        errorMap.put("TOUT-SEND-9999",errorResponse);

        //Time Out Security
        errorResponse = generateErrorResponse(McSendErrorCodes.TIMEOUT_ERROR,
                McSendErrorDescription.TIMEOUT_ERROR);
        errorMap.put("TOUT-SEC-9999",errorResponse);

        //Technical Issues
        errorResponse = generateErrorResponse(McSendErrorCodes.TECHNICAL_ERROR,
                McSendErrorDescription.TECHNICAL_ERROR);
        errorMap.put("SMGR-9999",errorResponse);

        //MC Send Technical Errors
        errorResponse = generateErrorResponse(McSendErrorCodes.TECHNICAL_ERROR,
                McSendErrorDescription.TECHNICAL_ERROR);
        errorMap.put("50005",errorResponse);
        errorMap.put("50007",errorResponse);
        errorMap.put("62000",errorResponse);
        errorMap.put("72000",errorResponse);
        errorMap.put("82000",errorResponse);
        errorMap.put("92000",errorResponse);
        errorMap.put("110501",errorResponse);
        errorMap.put("110506",errorResponse);
        errorMap.put("110507",errorResponse);
        errorMap.put("110509",errorResponse);
        errorMap.put("110510",errorResponse);
        errorMap.put("110511",errorResponse);
        errorMap.put("110515",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130004);
        errorMap.put("130004",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130005);
        errorMap.put("130005",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130006);
        errorMap.put("130006",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130007);
        errorMap.put("130007",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130008);
        errorMap.put("130008",errorResponse);

        //MC Send Business Errors
        errorResponse = generateErrorResponse(McSendErrorCodes.RESOURCE_UNKNOWN,
                McSendErrorDescription.DESC_110502);
        errorMap.put("110502",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.RESOURCE_ERROR,
                McSendErrorDescription.DESC_110503);
        errorMap.put("110503",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.RESOURCE_ERROR,
                McSendErrorDescription.DESC_110504);
        errorMap.put("110504",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.RESOURCE_ERROR,
                McSendErrorDescription.DESC_110505);
        errorMap.put("110505",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.RESOURCE_ERROR,
                McSendErrorDescription.DESC_110516);
        errorMap.put("110516",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130001);
        errorMap.put("130001",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130002);
        errorMap.put("130002",errorResponse);

        errorResponse = generateErrorResponse(McSendErrorCodes.DECLINE,
                McSendErrorDescription.DESC_130003);
        errorMap.put("130003",errorResponse);

        //Error mapping for Transfer Eligibility

        errorResponse = generateErrorResponse(McSendErrorCodes.RESOURCE_ERROR,
                McSendErrorDescription.DESC_110537);
        errorMap.put("110537",errorResponse);

        LOG.debug(McSendEnum.ERROR_MAPPER_FINISH.getLog());
    }

    private Error generateErrorResponse(String code,String description){
        Error error = new Error();
        error.setSource(source);
        error.setReasonCode(code);
        error.setDescription(description);
        error.setRecoverable(false);
        return error;
    }
}
