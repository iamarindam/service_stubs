package com.sm.mastercard.send.service;

import com.sm.mastercard.send.config.ErrorMapper;
import com.sm.mastercard.send.constants.McSendErrorCodes;
import com.sm.mastercard.send.constants.McSendErrorDescription;
import com.sm.mastercard.send.model.Error;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class errorMapperTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(errorMapperTest.class);

    @InjectMocks
    private static ErrorMapper errorMapper = new ErrorMapper();
    private static String source = "ZAPP";

    @BeforeClass
    public static void setUp() {
        ReflectionTestUtils.setField(errorMapper, "source", source);
        errorMapper.loadErrorMapper();
    }

    @Test
    public void test_getErrorMapper(){
        LOGGER.info("Came into method test_getErrorMapper() success.. ");
        Error expectedResponse = new Error();
        expectedResponse.setSource(source);
        expectedResponse.setReasonCode(McSendErrorCodes.STRUCTURAL_ERROR);
        expectedResponse.setDescription(McSendErrorDescription.STRUCTURAL_ERROR);
        expectedResponse.setRecoverable(false);

        Error actualResponse = errorMapper. getErrorMapper(McSendErrorCodes.STRUCTURAL_ERROR);

        Assertions.assertThat(actualResponse)
                .isEqualToComparingFieldByFieldRecursively(expectedResponse);
        LOGGER.info("Exiting method test_getErrorMapper() success.. ");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println(errorMapperTest.class+" has been tested and code coverage checked .. ");
    }
}
