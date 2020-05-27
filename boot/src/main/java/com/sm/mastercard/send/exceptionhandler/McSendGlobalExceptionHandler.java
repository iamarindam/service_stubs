package com.sm.mastercard.send.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.config.ErrorMapper;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.model.Error;
import com.sm.mastercard.send.model.*;
import com.sm.mastercard.send.util.AlertLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@ControllerAdvice
public class McSendGlobalExceptionHandler {
    private static final ZappLogger LOG = ZappLoggerFactory.getLogger(McSendGlobalExceptionHandler.class,true);

    @Autowired
    private ErrorMapper errorMapper;

    //400 - Bad request
    @ExceptionHandler({MethodArgumentNotValidException.class,ConstraintViolationException.class,
            MissingRequestHeaderException.class, InvalidFormatException.class,HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> generalValidationHandler(Exception ex, HttpServletRequest request) {
        ErrorResponse response = generateErrorResponse(McSendErrorCodes.STRUCTURAL_ERROR);
        LOG.error(logAlert(response, ex, request));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //500 INTERNAL_SERVER_ERROR
    @ExceptionHandler({Exception.class,Throwable.class,RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> processTechException(Exception ex, HttpServletRequest request) {
        //set technical error
        ErrorResponse response = generateErrorResponse(McSendErrorCodes.INTERNAL_TECHNICAL_ERROR);
        LOG.error(logAlert(response, ex, request));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String logAlert(ErrorResponse response, Exception ex, HttpServletRequest request) {
        StringWriter sw = new StringWriter();
        //ex.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        String stackStraceMsg = ex.getMessage();
        String participantId = request.getHeader(McSendConstants.X_PARTICIPANT_ID);

        ErrorAlertMessage eam = new ErrorAlertMessage();
        eam.setCorrId(UUID.randomUUID().toString());
        eam.setErrCode(response.getErrors().getError().get(0).getReasonCode());
        String errMsg = stackStraceMsg.length() >100 ? stackStraceMsg.substring(0, 100) : stackStraceMsg;
        eam.setErrMsgLength(stackStraceMsg.isEmpty() ? response.getErrors().getError().get(0).getDescription(): errMsg );
        eam.setErrStackStrace(exceptionAsString);
        eam.setParticipantId(participantId);
        return AlertLogMessage.getErrorAlertMessage(eam);
    }

    private ErrorResponse generateErrorResponse(String errorCode){
        ErrorResponse response = new ErrorResponse();
        ErrorResponseErrors errorResponseErrors = new ErrorResponseErrors();
        List<ErrorList> errorList = new ArrayList<>();
        ErrorList err = new ErrorList();

        Error error = errorMapper.getErrorMapper(errorCode);
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
