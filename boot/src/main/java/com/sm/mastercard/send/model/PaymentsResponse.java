package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentsResponse {
	private String authorizationCode;
	private String fundsAvailability;
	private String messageTypeIndicator;
	private String networkResponseCode;
	private String paymentAccountReference;
	private String paymentReference;
	private String processingCode;
	private String retrievalRequestNumber;
	private String paymentId;
	private String settlementAmount;
	private String settlementCurrency;
	private String switchSerialNumber;
	private String systemTraceAuditNumber;
	private String tokenRequestorId;
	private String transactionStatus;
	private String transactionStatusReason;
	private String transmissionDateTime;
	private String uniqueTransactionReference;

	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getFundsAvailability() {
		return fundsAvailability;
	}
	public void setFundsAvailability(String fundsAvailability) {
		this.fundsAvailability = fundsAvailability;
	}
	public String getMessageTypeIndicator() {
		return messageTypeIndicator;
	}
	public void setMessageTypeIndicator(String messageTypeIndicator) {
		this.messageTypeIndicator = messageTypeIndicator;
	}
	public String getNetworkResponseCode() {
		return networkResponseCode;
	}
	public void setNetworkResponseCode(String networkResponseCode) {
		this.networkResponseCode = networkResponseCode;
	}
	public String getPaymentAccountReference() {
		return paymentAccountReference;
	}
	public void setPaymentAccountReference(String paymentAccountReference) {
		this.paymentAccountReference = paymentAccountReference;
	}
	public String getPaymentReference() {
		return paymentReference;
	}
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}
	public String getProcessingCode() {
		return processingCode;
	}
	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}
	public String getRetrievalRequestNumber() {
		return retrievalRequestNumber;
	}
	public void setRetrievalRequestNumber(String retrievalRequestNumber) {
		this.retrievalRequestNumber = retrievalRequestNumber;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getSettlementAmount() {
		return settlementAmount;
	}
	public void setSettlementAmount(String settlementAmount) {
		this.settlementAmount = settlementAmount;
	}
	public String getSettlementCurrency() {
		return settlementCurrency;
	}
	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}
	public String getSwitchSerialNumber() {
		return switchSerialNumber;
	}
	public void setSwitchSerialNumber(String switchSerialNumber) {
		this.switchSerialNumber = switchSerialNumber;
	}
	public String getSystemTraceAuditNumber() {
		return systemTraceAuditNumber;
	}
	public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
		this.systemTraceAuditNumber = systemTraceAuditNumber;
	}
	public String getTokenRequestorId() {
		return tokenRequestorId;
	}
	public void setTokenRequestorId(String tokenRequestorId) {
		this.tokenRequestorId = tokenRequestorId;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionStatusReason() {
		return transactionStatusReason;
	}
	public void setTransactionStatusReason(String transactionStatusReason) {
		this.transactionStatusReason = transactionStatusReason;
	}
	public String getTransmissionDateTime() {
		return transmissionDateTime;
	}
	public void setTransmissionDateTime(String transmissionDateTime) {
		this.transmissionDateTime = transmissionDateTime;
	}
	public String getUniqueTransactionReference() {
		return uniqueTransactionReference;
	}
	public void setUniqueTransactionReference(String uniqueTransactionReference) {
		this.uniqueTransactionReference = uniqueTransactionReference;
	}
}
