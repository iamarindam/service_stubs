package integration.com.sm.mastercard.send.dataobject;

public class TransferEligibilityTestRequest {
    private String acquirerCountry;
    private Integer amount;
    private String currency;
    private String recipientAccountUri;
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

    public String getTransferAcceptorCountry() {
        return transferAcceptorCountry;
    }

    public void setTransferAcceptorCountry(String transferAcceptorCountry) {
        this.transferAcceptorCountry = transferAcceptorCountry;
    }
}
