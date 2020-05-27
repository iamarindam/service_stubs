package com.sm.mastercard.send.model;

/*
 *
 * @Author: Apeksha Ubhayakar
 * @Date: 11-12-2019
 *
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class SigningServiceResponse {
    @JsonProperty("verificationResult")
    private String signatureVerificationResult;

    @JsonProperty("Errors")
    private Errors errors;

    public String getSignatureVerificationResult() {
        return signatureVerificationResult;
    }

    public void setSignatureVerificationResult(String signatureVerificationResult) {
        this.signatureVerificationResult = signatureVerificationResult;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
