package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McTransferEligibilityResponse {
	private SendingReceivingEligibility receivingEligibility;
	private SendingReceivingEligibility sendingEligibility;
	private TransferEligibility transferEligibility;

	public SendingReceivingEligibility getReceivingEligibility() {
		return receivingEligibility;
	}

	public void setReceivingEligibility(SendingReceivingEligibility receivingEligibility) {
		this.receivingEligibility = receivingEligibility;
	}

	public SendingReceivingEligibility getSendingEligibility() {
		return sendingEligibility;
	}

	public void setSendingEligibility(SendingReceivingEligibility sendingEligibility) {
		this.sendingEligibility = sendingEligibility;
	}

	public TransferEligibility getTransferEligibility() {
		return transferEligibility;
	}

	public void setTransferEligibility(TransferEligibility transferEligibility) {
		this.transferEligibility = transferEligibility;
	}
}
