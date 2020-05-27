package com.sm.mastercard.send.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentsRequest {
    //private static final String REG_EXP = "^(\\d{4})\\D?(0[1-9]|1[0-2])\\D?([12]\\d|0[1-9]|3[01])T(\\D?([01]\\d|2[0-3])\\D?([0-5]\\d)\\D?([0-5]\\d)?\\D?(\\d{3})?)?Z$";

    @NotEmpty(message = "Payment reference cannot be empty")
    @NotBlank(message = "paymentReference cannot be blank")
    @Size(min = 1, max = 40,  message = "paymentReference size must be between 1 and 40")
    @JsonProperty("paymentReference")
    private String paymentReference;

    @NotEmpty(message = "Amount cannot be empty")
    @NotBlank(message = "Amount cannot be blank")
    @Size(min = 1, max = 12 , message = "Amount size must be between 1 and 12")
    @JsonProperty("amount")
    private String amount;

    @NotEmpty(message = "Currency cannot be empty")
    @NotBlank(message = "Currency cannot be blank")
    @JsonProperty("currency")
    private String currency;

    @NotNull (message = "Acquiring credentials cannot be null")
    @Valid
    @JsonProperty("acquiringCredentials")
    private AcquiringCredentials acquiringCredentials;

    @JsonProperty("deviceType")
    private String deviceType;

    @NotEmpty(message = "Funding source cannot be empty")
    @NotBlank(message = "Funding source cannot be blank")
    @JsonProperty("fundingSource")
    private String fundingSource;

    @JsonProperty("merchantCategoryCode")
    private String merchantCategoryCode; //SM -> McSend - default value - 6536 //properties file

    @Size(max = 30, message = "ParticipationId size cannot exceed 30")
    @JsonProperty("participationId")
    private String participationId;

    @JsonProperty("paymentType")
    private String paymentType; //SM -> McSend - default value - P2P //properties file

    @NotNull(message = "Recipient cannot be null")
    @Valid
    @JsonProperty("recipient")
    private Sender recipient;

    @NotNull(message = "Sender cannot be null")
    @Valid
    @JsonProperty("sender")
    private Sender sender;

    @NotEmpty(message = "Transaction local date and time cannot be empty")
    @NotBlank(message = "Transaction local date and time cannot be blank")
    @JsonProperty("transactionLocalDateTime")
   // @Pattern(regexp = REG_EXP)
    private String transactionLocalDateTime;

    @JsonProperty("transactionPurpose")
    private String transactionPurpose;

    @NotNull(message = "Transfer acceptor cannot be null")
    @Valid
    @JsonProperty("transferAcceptor")
    private TransferAcceptor transferAcceptor;

    @Size(max = 19, message = "Unique transaction reference size cannot exceed 19")
    @JsonProperty("uniqueTransactionReference")
    private String uniqueTransactionReference;

    public AcquiringCredentials getAcquiringCredentials() {
        return acquiringCredentials;
    }

    public void setAcquiringCredentials(AcquiringCredentials acquiringCredentials) {
        this.acquiringCredentials = acquiringCredentials;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getParticipationId() {
        return participationId;
    }

    public void setParticipationId(String participationId) {
        this.participationId = participationId;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Sender getRecipient() {
        return recipient;
    }

    public void setRecipient(Sender recipient) {
        this.recipient = recipient;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public String getTransactionLocalDateTime() {
        return transactionLocalDateTime;
    }

    public void setTransactionLocalDateTime(String transactionLocalDateTime) {
        this.transactionLocalDateTime = transactionLocalDateTime;
    }

    public String getTransactionPurpose() {
        return transactionPurpose;
    }

    public void setTransactionPurpose(String transactionPurpose) {
        this.transactionPurpose = transactionPurpose;
    }

    public TransferAcceptor getTransferAcceptor() {
        return transferAcceptor;
    }

    public void setTransferAcceptor(TransferAcceptor transferAcceptor) {
        this.transferAcceptor = transferAcceptor;
    }

    public String getUniqueTransactionReference() {
        return uniqueTransactionReference;
    }

    public void setUniqueTransactionReference(String uniqueTransactionReference) {
        this.uniqueTransactionReference = uniqueTransactionReference;
    }
}
