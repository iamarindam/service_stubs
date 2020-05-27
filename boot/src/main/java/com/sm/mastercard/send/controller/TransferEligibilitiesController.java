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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.constants.McSendEnum;
import com.sm.mastercard.send.model.TransferEligibilityRequest;
import com.sm.mastercard.send.service.TransferEligibilityService;
import com.sm.mastercard.send.util.McSendUtil;

import io.swagger.annotations.ApiParam;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RestController
@RequestMapping(McSendConstants.URL)
@Validated
public class TransferEligibilitiesController implements TransferEligibilitiesControllerApi{
	private static final ZappLogger LOG = ZappLoggerFactory.getLogger(TransferEligibilitiesController.class,true);

	private HttpServletRequest request;

	@Autowired
	private TransferEligibilityService transferEligibilityService;

	public TransferEligibilitiesController() {}

	@Autowired
	public TransferEligibilitiesController(HttpServletRequest request) {
		this.request = request;
	}

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
			@RequestHeader(value=McSendConstants.X_ENCRYPTED, required=false) Boolean xEncrypted
	) throws IOException {
		LOG.info(McSendEnum.TE_START.getLog());

		String correlationId = McSendUtil.generateCorrelationId(request);
		LOG.debug(McSendEnum.TE_CORR_ID.getLog() , correlationId);
		try {
			return transferEligibilityService.initiateTransferEligibility(transferEligibilityRequest, correlationId,
					xParticipantId, xBusinessMessageIdentifier, xJWSSignature, xEncrypted);
		}finally {
			LOG.info(McSendEnum.TE_CONTROLLER_END.getLog() , correlationId);
		}
	}

	@PostMapping(value = McSendConstants.TRANSFER_ELIGIBILITY_URL+"-test/generate/token", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	ResponseEntity<String> generateJwsToken(@RequestBody TransferEligibilityRequest transferEligibilityRequest){
		LOG.info(McSendEnum.TE_SIGNATURE_GEN.getLog());
		return transferEligibilityService.generateJwsToken(transferEligibilityRequest);
	}
}
