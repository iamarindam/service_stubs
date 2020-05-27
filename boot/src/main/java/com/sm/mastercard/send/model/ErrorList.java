package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;



@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorList {
	  @JsonProperty("source")
	  private String source;

	  @JsonProperty("reasonCode")
	  private String reasonCode;

	  @JsonProperty("description")
	  private String description;

	  @JsonProperty("recoverable")
	  private Boolean recoverable;


	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getRecoverable() {
		return recoverable;
	}

	public void setRecoverable(Boolean recoverable) {
		this.recoverable = recoverable;
	}
}
