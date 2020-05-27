package com.sm.mastercard.send.service;

import com.sm.mastercard.send.config.ErrorMapper;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.constants.McSendErrorDescription;
import com.sm.mastercard.send.model.Error;
import com.sm.mastercard.send.model.ErrorResponse;
import com.sm.mastercard.send.model.McErrorResponse;
import com.sm.mastercard.send.util.McSendTestUtil;
import com.sm.mastercard.send.util.McSendUtil;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class SendUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferEligibilityServiceTest.class);

    @InjectMocks
    private static McSendUtil mcSendUtil = new McSendUtil();

    @InjectMocks
    private McSendTestUtil mcSendTestUtil = new McSendTestUtil();

    @Mock
    private static ErrorResponse errorResponse;

    @Mock
    private static McErrorResponse mcErrorResponse;

    @Mock
    private static ErrorMapper errorMapper;

    @Mock
    static
    HttpServletRequest httpServletRequest;

    @BeforeClass
    public static void setUp() {
        errorResponse = Mockito.mock(ErrorResponse.class);
        mcErrorResponse = Mockito.mock(McErrorResponse.class);
        errorMapper = Mockito.mock(ErrorMapper.class);
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        ReflectionTestUtils.setField(mcSendUtil, "source", "ZAPP");
        ReflectionTestUtils.setField(mcSendUtil, "consumerkey", "8qoFNpN4qMiy0jKXScZ1yuSlmMWnPCIhx5KglNUPd652e7d4!368e82a6299d4738bf4f8132dc0b029f0000000000000000");
        ReflectionTestUtils.setField(mcSendUtil, "privateKeyPath", "/payments/ServiceManagerZapp-sandbox.p12");
    }

    @Test
    public void test_generateErrorResponseToAHI()throws IOException, URISyntaxException {
        LOGGER.info("Came into method test_generateErrorResponseToAHI().. ");

        //Building input request
        URL response = SendHandlerTest.class.getResource(mcSendTestUtil.getError_response());
        File responseFile = Paths.get(response.toURI()).toFile();
        errorResponse = McSendTestUtil.objectMapper().readValue(responseFile, ErrorResponse.class);

        URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getMc_error());
        File requestFile = Paths.get(request.toURI()).toFile();
        mcErrorResponse = McSendTestUtil.objectMapper().readValue(requestFile, McErrorResponse.class);

        Error error = new Error();
        error.setSource("ZAPP");
        error.setReasonCode(McSendErrorCodes.TECHNICAL_ERROR);
        error.setDescription(McSendErrorDescription.TECHNICAL_ERROR);
        error.setRecoverable(false);

        String errorCode = mcErrorResponse.getErrors().getError().get(0).getErrorDetailCode();
        Mockito.when(errorMapper.getErrorMapper(errorCode)).thenReturn(error);

        Message<?> message = MessageBuilder.withPayload(mcErrorResponse).build();
        //actual
        ResponseEntity<Object> actualResponse =  mcSendUtil.
                generateErrorResponseToAHI(HttpStatus.BAD_REQUEST,message);

        Assertions.assertThat(actualResponse.getBody())
                .isEqualToComparingFieldByFieldRecursively(errorResponse);

        LOGGER.info("Exiting method test_generateErrorResponseToAHI().. ");
    }

    @Test
    public void test_generateErrorResponseToAHI_alternate()throws IOException, URISyntaxException {
        LOGGER.info("Came into method test_generateErrorResponseToAHI_alternate().. ");

        //Building input request
        URL response = SendHandlerTest.class.getResource(mcSendTestUtil.getError_response());
        File responseFile = Paths.get(response.toURI()).toFile();
        errorResponse = McSendTestUtil.objectMapper().readValue(responseFile, ErrorResponse.class);

        URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getMc_error());
        File requestFile = Paths.get(request.toURI()).toFile();
        mcErrorResponse = McSendTestUtil.objectMapper().readValue(requestFile, McErrorResponse.class);
        mcErrorResponse.getErrors().getError().get(0);
        mcErrorResponse.getErrors().getError().get(0).setReasonCode("50005");
        mcErrorResponse.getErrors().getError().get(0).setErrorDetailCode(null);


        Error error = new Error();
        error.setSource("ZAPP");
        error.setReasonCode(McSendErrorCodes.TECHNICAL_ERROR);
        error.setDescription(McSendErrorDescription.TECHNICAL_ERROR);
        error.setRecoverable(false);

        String errorCode = mcErrorResponse.getErrors().getError().get(0).getReasonCode();
        Mockito.when(errorMapper.getErrorMapper(errorCode)).thenReturn(error);

        Message<?> message = MessageBuilder.withPayload(mcErrorResponse).build();
        //actual
        ResponseEntity<Object> actualResponse =  mcSendUtil.
                generateErrorResponseToAHI(HttpStatus.BAD_REQUEST,message);

        Assertions.assertThat(actualResponse.getBody())
                .isEqualToComparingFieldByFieldRecursively(errorResponse);

        LOGGER.info("Exiting method test_generateErrorResponseToAHI_alternate().. ");
    }

    @Test
    public void test_generateErrorResponseToAHI1()throws IOException, URISyntaxException {
        LOGGER.info("Came into method test_generateErrorResponseToAHI1().. ");

        //Building input request
        URL response = SendHandlerTest.class.getResource(mcSendTestUtil.getError_response());
        File responseFile = Paths.get(response.toURI()).toFile();
        errorResponse = McSendTestUtil.objectMapper().readValue(responseFile, ErrorResponse.class);
        errorResponse.getErrors().getError().get(0).setReasonCode(McSendErrorCodes.RESOURCE_ERROR);
        errorResponse.getErrors().getError().get(0).setDescription(McSendErrorDescription.DESC_110503);


        URL request = SendHandlerTest.class.getResource(mcSendTestUtil.getMc_error());
        File requestFile = Paths.get(request.toURI()).toFile();
        mcErrorResponse = McSendTestUtil.objectMapper().readValue(requestFile, McErrorResponse.class);
        mcErrorResponse.getErrors().getError().get(0).setErrorDetailCode("110503");
        mcErrorResponse.getErrors().getError().get(0).setDescription(McSendErrorDescription.DESC_110503);

        Error error = new Error();
        error.setSource("ZAPP");
        error.setReasonCode(McSendErrorCodes.RESOURCE_ERROR);
        error.setDescription(McSendErrorDescription.DESC_110503);
        error.setRecoverable(false);

        String errorCode = mcErrorResponse.getErrors().getError().get(0).getErrorDetailCode();
        Mockito.when(errorMapper.getErrorMapper(errorCode)).thenReturn(error);

        Message<?> message = MessageBuilder.withPayload(mcErrorResponse).build();
        //actual
        ResponseEntity<Object> actualResponse =  mcSendUtil.
                generateErrorResponseToAHI(HttpStatus.BAD_REQUEST,message);

        Assertions.assertThat(actualResponse.getBody())
                .isEqualToComparingFieldByFieldRecursively(errorResponse);

        LOGGER.info("Exiting method test_generateErrorResponseToAHI1().. ");
    }
}
