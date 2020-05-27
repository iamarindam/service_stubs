package integration.com.sm.mastercard.send.dataobject;

import com.sm.mastercard.send.model.AcquiringCredentials;
import com.sm.mastercard.send.model.Sender;
import com.sm.mastercard.send.model.TransferAcceptor;

public class PaymentsRequest {

    private String paymentReference;
    private String amount;
    private String currency;
    private AcquiringCredentials acquiringCredentials;
    private String deviceType;
    private String fundingSource;
    private String participationId;
    private Sender recipient;
    private Sender sender;
    private String transactionLocalDateTime;
    private String transactionPurpose;
    private TransferAcceptor transferAcceptor;
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
