package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    @JsonProperty("Source")
    private String source;

    @JsonProperty("ReasonCode")
    private String reasonCode;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Recoverable")
    private boolean recoverable;

    @JsonProperty("ErrorDetailCode")
    private String errorDetailCode;

    @JsonProperty("RequestId")
    private String requestId;

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

    public boolean isRecoverable() {
        return recoverable;
    }

    public void setRecoverable(boolean recoverable) {
        this.recoverable = recoverable;
    }

    public String getErrorDetailCode() {
        return errorDetailCode;
    }

    public void setErrorDetailCode(String errorDetailCode) {
        this.errorDetailCode = errorDetailCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
