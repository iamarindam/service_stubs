package com.sm.mastercard.send.constants;

public final class McSendConstants {
	private McSendConstants() {}

	public static final String BAD_REQUEST = "BAD_REQUEST";
	public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	public static final String DEFAULT_RESPONSE = "default response";

	public static final String URL = "/zapp/p2p";
	public static final String TRANSFER_ELIGIBILITY_URL = "/transfer-eligibilities";
	public static final String PAYMENTS_URL = "/payment-executions";
	public static final String CORRELATION_ID = "X-Correlation-Id";
	public static final String X_JWS_SIGNATURE = "X-JWS-Signature";
	public static final String X_PARTICIPANT_ID = "X-Participant-Id";
	public static final String X_PARTNER_ID = "X-Partner-Id";
	public static final String X_BUSINESS_MESSAGE_IDENTIFIER = "X-Business-Message-Identifier";
	public static final String X_DELIVERY_CONTROL = "X-Delivery-Control";
	public static final String X_ENCRYPTED = "X-Encrypted";
	public static final String AUTHORIZATION = "Authorization";
	public static final String X_REPEAT_FLAG = "X-Repeat-Flag";

	public static final String SEND_PARTICIPANT_PARAM = "X-Participant-Id of sender issued by mastercard";
	public static final String SEND_BMIDENT_PARAM = "Sequence Number set by sending party";
	public static final String SEND_PARTICIPANT_SIZE = "ParticipantId length should not more than 35";
	public static final String SEND_BMIDENT_SIZE = "BusinessMsgIdentifier length should not more than 40";
	public static final String SEND_SIGNATURE_PARAM = "X-JWS-Signature set by sending party";
	public static final String SEND_ENCRYPTED_PARAM = "The value of this header depends on encryption " +
			"preferences set when onboarding the partner. " +
			"This header should not be provided if encryption is disabled for the partner." +
			" The header must be 'true' if encryption is 'enabled' for the partner (i.e. required). " +
			"The header can be 'true' or 'false' if encryption is 'flexible' for the partner.";
	public static final String SEND_REPEAT_PARAM = "X-Repeat-Flag set by sending party";

	public static final String HTTP_STATUSCODE = "http_statusCode";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";

}
