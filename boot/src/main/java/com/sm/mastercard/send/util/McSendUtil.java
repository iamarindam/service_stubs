package com.sm.mastercard.send.util;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.mastercard.developer.oauth.OAuth;
import com.mastercard.developer.utils.AuthenticationUtils;
import com.sm.mastercard.send.config.ErrorMapper;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.model.Error;
import com.sm.mastercard.send.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class McSendUtil {
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(McSendUtil.class,true);

    @Value("${send.sandbox.consumerkey}")
    private String consumerkey;

    @Value("${send.sandbox.privatekey}")
    private String privateKeyPath;

    @Value("${send.validation.source}")
    private String source;

    @Autowired
    private ErrorMapper errorMapper;

    public static String generateCorrelationId(HttpServletRequest request) {
        if (null != request) {
            return request.getHeader(McSendConstants.CORRELATION_ID);
        } else {
            //For testing
            return UUID.randomUUID().toString();
        }
    }

    public String generateAuthTokenForMcSend(String url,String payload) {
        try {
            PrivateKey signingKey = AuthenticationUtils.loadSigningKey(
                    privateKeyPath,
                    "keyalias",
                    "keystorepassword");

            String consumerKey = consumerkey;
            URI uri = URI.create(url);
            String method = "POST";
            Charset charset = StandardCharsets.UTF_8;

            return OAuth.getAuthorizationHeader(uri, method, payload, charset, consumerKey, signingKey);
        } catch (Exception e) {
            LOG.error("Exception occurred at generateAuthTokenForMcSend");
            return "";
        }
    }

    public ResponseEntity<Object> generateErrorResponseToAHI(HttpStatus statusCode, Message<?> errorResponseMessage) {
        //error response sent to OI from SM
        McErrorResponse mcErrorResponse = (McErrorResponse) errorResponseMessage.getPayload();
        ErrorResponse errorResponse = generateErrorResponse(mcErrorResponse);
        if(!McSendErrorCodes.TIMEOUT_ERROR.equals(errorResponse.getErrors().getError().get(0).getReasonCode())){
            statusCode = HttpStatus.NOT_ACCEPTABLE;
        }
        return new ResponseEntity<>(errorResponse, statusCode);
    }

    private ErrorResponse generateErrorResponse(McErrorResponse mcErrorResponse) {
        ErrorResponse response = new ErrorResponse();
        ErrorResponseErrors errorResponseErrors = new ErrorResponseErrors();
        List<ErrorList> errorList = new ArrayList<>();
        ErrorList err = new ErrorList();

        Error error;
        String errorCode = mcErrorResponse.getErrors().getError().get(0).getErrorDetailCode();
        if (null != errorCode && !errorCode.isEmpty()) {
            error = errorMapper.getErrorMapper(errorCode);
            if (McSendErrorCodes.ERROR_CODE_110503.equals(errorCode) || McSendErrorCodes.ERROR_CODE_130004.equals(errorCode)) {
                error.setDescription(mcErrorResponse.getErrors().getError().get(0).getDescription());
            }
        } else {
            error = errorMapper.getErrorMapper(mcErrorResponse.getErrors().getError().get(0).getReasonCode());
        }

        err.setSource(error.getSource());
        err.setReasonCode(error.getReasonCode());
        err.setDescription(error.getDescription());
        err.setRecoverable(error.isRecoverable());

        errorList.add(err);
        errorResponseErrors.setError(errorList);
        response.setErrors(errorResponseErrors);
        return response;
    }
}
