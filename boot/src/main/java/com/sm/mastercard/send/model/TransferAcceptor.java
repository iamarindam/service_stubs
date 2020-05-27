package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferAcceptor {

    @NotNull(message = "Address cannot be null")
    @Valid
    @JsonProperty("address")
    private Address address;

    @Size(min=1, max= 12, message = "Convenience amount size cannot exceed 12")
    @JsonProperty("convenienceAmount")
    private String convenienceAmount;

    @JsonProperty("convenienceIndicator")
    private String convenienceIndicator;

    @NotEmpty(message = "Id cannot be empty")
    @NotBlank(message = "Id cannot be blank")
    @Size(min = 1, max = 15, message = "Id size must be between 1 and 15")
    @JsonProperty("id")
    private String id;

    @JsonProperty("mastercardAssignedMerchantId")
    private String mastercardAssignedMerchantId;

    @Size(min = 1, max= 22, message = "Name must be between 1 and 22")
    @JsonProperty("name")
    private String name;

    @Size(max= 11, message = "Payment facilitator Id size cannot exceed 11")
    @JsonProperty("paymentFacilitatorId")
    private String paymentFacilitatorId;

    @Size(max= 15, message = "Sub merchant Id size cannot exceed 15")
    @JsonProperty("subMerchantId")
    private String subMerchantId;

    @NotEmpty(message = "terminalId cannot be empty")
    @NotBlank(message = "terminalId cannot be blank")
    @Size(min = 1, max = 8, message = "TerminalId size must be between 1 and 8")
    @JsonProperty("terminalId")
    private String terminalId;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getConvenienceAmount() {
        return convenienceAmount;
    }

    public void setConvenienceAmount(String convenienceAmount) {
        this.convenienceAmount = convenienceAmount;
    }

    public String getConvenienceIndicator() {
        return convenienceIndicator;
    }

    public void setConvenienceIndicator(String convenienceIndicator) {
        this.convenienceIndicator = convenienceIndicator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMastercardAssignedMerchantId() {
        return mastercardAssignedMerchantId;
    }

    public void setMastercardAssignedMerchantId(String mastercardAssignedMerchantId) {
        this.mastercardAssignedMerchantId = mastercardAssignedMerchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentFacilitatorId() {
        return paymentFacilitatorId;
    }

    public void setPaymentFacilitatorId(String paymentFacilitatorId) {
        this.paymentFacilitatorId = paymentFacilitatorId;
    }

    public String getSubMerchantId() {
        return subMerchantId;
    }

    public void setSubMerchantId(String subMerchantId) {
        this.subMerchantId = subMerchantId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
