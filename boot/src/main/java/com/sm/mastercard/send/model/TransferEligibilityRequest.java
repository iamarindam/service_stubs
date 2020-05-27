package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferEligibilityRequest {
    @NotNull(message="aquirerCountry code cannot be null")
    @NotBlank
    @NotEmpty
    @Size(max=3)
    @JsonProperty("acquirerCountry")
    private String acquirerCountry;

    @Digits(integer=12, fraction = 0)
    @JsonProperty("amount")
    private Integer amount;

    @Size(max=1)
    @JsonProperty("crossBorderEligible")
    private String crossBorderEligible;

    @Size(max=3)
    @JsonProperty("currency")
    private String currency;

    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty("recipientAccountUri")
    private String recipientAccountUri;

    @JsonProperty("paymentType")
    private String paymentType;

    @NotNull(message= "transferAcceptorCountry cannot be null")
    @NotBlank
    @NotEmpty
    @Size(max=3)
    @JsonProperty("transferAcceptorCountry")
    private String transferAcceptorCountry;

    public String getAcquirerCountry() {
        return acquirerCountry;
    }

    public void setAcquirerCountry(String acquirerCountry) {
        this.acquirerCountry = acquirerCountry;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCrossBorderEligible() {
        return crossBorderEligible;
    }

    public void setCrossBorderEligible(String crossBorderEligible) {
        this.crossBorderEligible = crossBorderEligible;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRecipientAccountUri() {
        return recipientAccountUri;
    }

    public void setRecipientAccountUri(String recipientAccountUri) {
        this.recipientAccountUri = recipientAccountUri;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransferAcceptorCountry() {
        return transferAcceptorCountry;
    }

    public void setTransferAcceptorCountry(String transferAcceptorCountry) {
        this.transferAcceptorCountry = transferAcceptorCountry;
    }
}
