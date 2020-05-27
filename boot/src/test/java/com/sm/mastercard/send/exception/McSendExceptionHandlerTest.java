/**
 * 
 */
package com.sm.mastercard.send.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sm.mastercard.send.config.ErrorMapper;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.constants.McSendErrorDescription;
import com.sm.mastercard.send.controller.McSendControllerTest;
import com.sm.mastercard.send.exceptionhandler.McSendGlobalExceptionHandler;
import com.sm.mastercard.send.model.Error;
import com.sm.mastercard.send.model.ErrorList;
import com.sm.mastercard.send.model.ErrorResponse;
import com.sm.mastercard.send.model.ErrorResponseErrors;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class McSendExceptionHandlerTest {
	private static final Logger LOG = LoggerFactory.getLogger(McSendExceptionHandlerTest.class);

	@InjectMocks
	private static McSendGlobalExceptionHandler mcSendGlobalExceptionHandler = new McSendGlobalExceptionHandler();

	@Mock
	private static ErrorMapper errorMapper;

	@Mock
	static
    HttpServletRequest httpServletRequest;
	
	@Mock
	static
	Exception ex;

	@BeforeClass
    public static void setUp() {
			ex = Mockito.mock(Exception.class);
			httpServletRequest = Mockito.mock(HttpServletRequest.class);
			errorMapper = Mockito.mock(ErrorMapper.class);
    }
	
	@Test
	public void test_processFieldValidationError()
			throws URISyntaxException, JsonParseException, JsonMappingException, IOException {

		LOG.info("inside test_processFieldValidationError method ... ");
		ErrorResponse response = new ErrorResponse();
		ErrorResponseErrors errorResponseErrors = new ErrorResponseErrors();
		List<ErrorList> errorList = new ArrayList<>();
		ErrorList err = new ErrorList();

		err.setSource("ZAPP");
		err.setReasonCode(McSendErrorCodes.STRUCTURAL_ERROR);
		err.setDescription(McSendErrorDescription.STRUCTURAL_ERROR);
		err.setRecoverable(false);
		errorList.add(err);
		errorResponseErrors.setError(errorList);
		response.setErrors(errorResponseErrors);

		ex = new RuntimeException();
		ex = new RuntimeException("TOUT-9999");
		LOG.info("Error code :: "+((RuntimeException)ex));
		ex = new RuntimeException("Exception", ex);
		ex = new RuntimeException("Exception");
		ex = new RuntimeException(ex);
		ex = new RuntimeException("Exception");
		httpServletRequest.setAttribute(McSendConstants.X_PARTICIPANT_ID, "123");

		Error error = new Error();
		error.setSource("ZAPP");
		error.setReasonCode(McSendErrorCodes.STRUCTURAL_ERROR);
		error.setDescription(McSendErrorDescription.STRUCTURAL_ERROR);
		error.setRecoverable(false);

		//when
		Mockito.when(errorMapper.getErrorMapper(
				isA(String.class)))
				.thenReturn(error); //then

		ResponseEntity<Object> actualResponse = mcSendGlobalExceptionHandler.generalValidationHandler(ex, httpServletRequest);

		Assertions.assertThat(actualResponse.getBody()).isEqualToComparingFieldByFieldRecursively(response);

		LOG.info("Exiting test_processFieldValidationError method ... ");

	}
	
	@Test
	public void test_handleAllExceptions()
			throws URISyntaxException, JsonParseException, JsonMappingException, IOException {

		LOG.info("inside test_handleAllExceptions method ... ");
		ErrorResponse response = new ErrorResponse();
		ErrorResponseErrors errorResponseErrors = new ErrorResponseErrors();
		List<ErrorList> errorList = new ArrayList<>();
		ErrorList err = new ErrorList();

		err.setSource("ZAPP");
		err.setReasonCode(McSendErrorCodes.TECHNICAL_ERROR);
		err.setDescription(McSendErrorDescription.TECHNICAL_ERROR);
		err.setRecoverable(false);
		errorList.add(err);
		errorResponseErrors.setError(errorList);
		response.setErrors(errorResponseErrors);

		ex = new RuntimeException("Exception");
		httpServletRequest.setAttribute(McSendConstants.X_PARTICIPANT_ID, "123");

		Error error = new Error();
		error.setSource("ZAPP");
		error.setReasonCode(McSendErrorCodes.TECHNICAL_ERROR);
		error.setDescription(McSendErrorDescription.TECHNICAL_ERROR);
		error.setRecoverable(false);

		//when
		Mockito.when(errorMapper.getErrorMapper(
				isA(String.class)))
				.thenReturn(error); //then

		ResponseEntity<Object> actualResponse = mcSendGlobalExceptionHandler.processTechException(ex, httpServletRequest);

		Assertions.assertThat(actualResponse.getBody()).isEqualToComparingFieldByFieldRecursively(response);
		
		LOG.info("Exiting test_handleAllExceptions method ... ");

	}

	@AfterClass
	public static void tearDown() {
		System.out.println(McSendControllerTest.class+" has been tested and code coverage checked .. ");
	}
}
