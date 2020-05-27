package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleMessage {
    @NotEmpty(message = "Acquiring identification code cannot be empty")
    @NotBlank(message = "Acquiring identification code cannot be blank")
    @Size(min = 1, max = 9, message = "Acquiring identification code size must be between 1 and 9")
    @JsonProperty("acquiringIdentificationCode")
    private String acquiringIdentificationCode;

    @NotEmpty(message = "ProcessorId cannot be empty")
    @NotBlank(message = "ProcessorId cannot be blank")
    @Size(min = 1, max = 10, message = "ProcessorId size must be between 1 and 10")
    @JsonProperty("processorId")
    private String processorId;

    public String getAcquiringIdentificationCode() {
        return acquiringIdentificationCode;
    }

    public void setAcquiringIdentificationCode(String acquiringIdentificationCode) {
        this.acquiringIdentificationCode = acquiringIdentificationCode;
    }

    public String getProcessorId() {
        return processorId;
    }

    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }
}
