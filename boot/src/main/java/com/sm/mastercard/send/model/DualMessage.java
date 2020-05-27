package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class DualMessage {
    @NotEmpty(message = "AcquiringBin cannot be empty")
    @NotBlank(message = "AcquiringBin cannot be blank")
    @Size(min = 1, max = 6, message = "AcquiringBin size must be between 1 and 6")
    @JsonProperty("acquiringBin")
    private String acquiringBin;

    public String getAcquiringBin() {
        return acquiringBin;
    }

    public void setAcquiringBin(String acquiringBin) {
        this.acquiringBin = acquiringBin;
    }

}
