package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcquiringCredentials {

    @JsonProperty("acquiringCountry")
    private String acquiringCountry;

    @NotEmpty(message = "AcquiringIca cannot be empty")
    @NotBlank(message = "AcquiringIca cannot be blank")
    @Size(min=1 , max= 11, message = "AcquiringIca size must be between 1 and 11")
    @JsonProperty("acquiringIca")
    private String acquiringIca;

    @NotNull(message = "dualMessage cannot be null")
    @Valid
    @JsonProperty("dualMessage")
    private DualMessage dualMessage;

    @NotNull(message = "singleMessage cannot be null")
    @Valid
    @JsonProperty("singleMessage")
    private SingleMessage singleMessage;

    public String getAcquiringCountry() {
        return acquiringCountry;
    }

    public void setAcquiringCountry(String acquiringCountry) {
        this.acquiringCountry = acquiringCountry;
    }

    public String getAcquiringIca() {
        return acquiringIca;
    }

    public void setAcquiringIca(String acquiringIca) {
        this.acquiringIca = acquiringIca;
    }

    public DualMessage getDualMessage() {
        return dualMessage;
    }

    public void setDualMessage(DualMessage dualMessage) {
        this.dualMessage = dualMessage;
    }

    public SingleMessage getSingleMessage() {
        return singleMessage;
    }

    public void setSingleMessage(SingleMessage singleMessage) {
        this.singleMessage = singleMessage;
    }
}
