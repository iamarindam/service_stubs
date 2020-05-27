package com.sm.mastercard.send.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configurable
public class McSendTestUtil {
    private static String participantId = "002907";
    private static String correlationId = "1234";
    private static String businessMsgIdentifier = "zapp";
    private static String signature="brillioSig";
    private static String partnerId="123456";
    
    private String oiToSmRequest_payments = "/data/payments/oiToSmRequest.json";
    private String smToOiResponse_payments = "/data/payments/smToOiResponse.json";

    private String oiToSmRequest_te = "/data/transferEligibility/oiToSmRequest.json";
	private String smToOiResponse_te = "/data/transferEligibility/smToOiResponse.json";

	private String error_response = "/data/transferEligibility/smToOiResNegative.json";
	private String mc_error = "/data/transferEligibility/mcError.json";


	public String getOiToSmRequest_payments() {
		return oiToSmRequest_payments;
	}

	public String getSmToOiResponse_payments() {
		return smToOiResponse_payments;
	}

	public String getOiToSmRequest_te() {
		return oiToSmRequest_te;
	}

	public String getSmToOiResponse_te() {
		return smToOiResponse_te;
	}

	public String getError_response() {
		return error_response;
	}

	public String getMc_error() {
		return mc_error;
	}

	/**
	 * @return the participantId
	 */
	public static String getParticipantId() {
		return participantId;
	}

	/**
	 * @param participantId the participantId to set
	 */
	public static void setParticipantId(String participantId) {
		McSendTestUtil.participantId = participantId;
	}

	/**
	 * @return the correlationId
	 */
	public static String getCorrelationId() {
		return correlationId;
	}

	/**
	 * @param correlationId the correlationId to set
	 */
	public static void setCorrelationId(String correlationId) {
		McSendTestUtil.correlationId = correlationId;
	}

	/**
	 * @return the businessMsgIdentifier
	 */
	public static String getBusinessMsgIdentifier() {
		return businessMsgIdentifier;
	}

	/**
	 * @return the signature
	 */
	public static String getSignature() {
		return signature;
	}

	/**
	 * @return the partnerId
	 */
	public static String getPartnerId() {
		return partnerId;
	}

	@Bean
	public static ObjectMapper objectMapper() {
		JavaTimeModule module = new JavaTimeModule();
		LocalDateTimeDeserializer localDateTimeDeserializer =  new
				LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
		module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
		ObjectMapper objectMapperObj = Jackson2ObjectMapperBuilder.json()
				.modules(module)
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();
		return objectMapperObj ;
	}

}
