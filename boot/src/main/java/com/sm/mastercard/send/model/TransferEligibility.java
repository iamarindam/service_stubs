package com.sm.mastercard.send.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.annotation.Validated;

@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferEligibility {

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	private boolean eligible;
}
