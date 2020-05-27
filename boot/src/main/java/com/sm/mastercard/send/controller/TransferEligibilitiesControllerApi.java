/**
 * 
 */
package com.sm.mastercard.send.controller;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.model.ErrorResponse;
import com.sm.mastercard.send.model.McTransferEligibilityResponse;
import com.sm.mastercard.send.model.TransferEligibilityRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

/**
 * @author Arindam.Seal
 *
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-01-29T21:32:48.896Z")
@Api(value = "transferEligibilityService", description = "the transferEligibilityService API")
public interface TransferEligibilitiesControllerApi {

	public static final String CORR_ID = "The Correlation ID";

	@ApiOperation(value = "transferEligibilityService", nickname = "transferEligibilityService", notes = "", response = McTransferEligibilityResponse.class, tags={ "transfer-eligibilities-api-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = McTransferEligibilityResponse.class,
                    responseHeaders = {
                            @ResponseHeader(name = McSendConstants.CORRELATION_ID, description = CORR_ID, response = String.class),
                    }
            ),
            @ApiResponse(code = 400, message = McSendConstants.BAD_REQUEST, response = ErrorResponse.class,
                    responseHeaders = {
                            @ResponseHeader(name = McSendConstants.CORRELATION_ID, description = CORR_ID, response = String.class)
                    }
            ),
            @ApiResponse(code = 406, message = "Client Error", response = ErrorResponse.class,
            responseHeaders = {
                    @ResponseHeader(name = McSendConstants.CORRELATION_ID, description = CORR_ID, response = String.class)
            }
            ),
            @ApiResponse(code = 500, message = McSendConstants.INTERNAL_SERVER_ERROR, response = ErrorResponse.class,
                    responseHeaders = {
                            @ResponseHeader(name = McSendConstants.CORRELATION_ID, description = CORR_ID, response = String.class)
                    }
            )
    })
	@PostMapping(value = McSendConstants.TRANSFER_ELIGIBILITY_URL, consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Object> transferEligibilityService(
	@ApiParam(value = McSendConstants.SEND_PARTICIPANT_PARAM)
	@RequestHeader(value=McSendConstants.X_PARTICIPANT_ID,required=true)
	@Size(max= 35, message = McSendConstants.SEND_PARTICIPANT_SIZE)  String xParticipantId

	,@ApiParam(value = McSendConstants.SEND_BMIDENT_PARAM)
	@RequestHeader(value=McSendConstants.X_BUSINESS_MESSAGE_IDENTIFIER,required=true)
	@Size(max= 40, message = McSendConstants.SEND_BMIDENT_SIZE) String xBusinessMessageIdentifier

	,@ApiParam(value = McSendConstants.SEND_SIGNATURE_PARAM)
	@RequestHeader(value=McSendConstants.X_JWS_SIGNATURE,required=true) String xJWSSignature

	,@ApiParam @Valid @RequestBody TransferEligibilityRequest transferEligibilityRequest

	,@ApiParam(value = McSendConstants.SEND_ENCRYPTED_PARAM)
	@RequestHeader(value=McSendConstants.X_ENCRYPTED, required=false) Boolean xEncrypted ) throws JsonProcessingException, IOException;
}
