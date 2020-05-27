package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class McErrorResponse {
    @JsonProperty("Errors")
    private Errors errors;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
