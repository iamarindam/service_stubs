package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendingReceivingEligibility {
	private String accountStatementCurrency;
	private String brand;
	private boolean eligible;
	private String enablementIndicator;
	private String fundsAvailability;
	private String institutionCountry;
	private String institutionName;
	private String processingType;
	private String productType;
	private String reasonCode;
	private String reasonDescription;
	private String type;
	private String acceptanceBrand;
	private String brandCode;
	private String issuingICA;
	private Boolean crossBorder;
	
	public String getAccountStatementCurrency() {
		return accountStatementCurrency;
	}
	public void setAccountStatementCurrency(String accountStatementCurrency) {
		this.accountStatementCurrency = accountStatementCurrency;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public boolean isEligible() {
		return eligible;
	}
	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}
	public String getEnablementIndicator() {
		return enablementIndicator;
	}
	public void setEnablementIndicator(String enablementIndicator) {
		this.enablementIndicator = enablementIndicator;
	}
	public String getFundsAvailability() {
		return fundsAvailability;
	}
	public void setFundsAvailability(String fundsAvailability) {
		this.fundsAvailability = fundsAvailability;
	}
	public String getInstitutionCountry() {
		return institutionCountry;
	}
	public void setInstitutionCountry(String institutionCountry) {
		this.institutionCountry = institutionCountry;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getProcessingType() {
		return processingType;
	}
	public void setProcessingType(String processingType) {
		this.processingType = processingType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonDescription() {
		return reasonDescription;
	}
	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAcceptanceBrand() {
		return acceptanceBrand;
	}
	public void setAcceptanceBrand(String acceptanceBrand) {
		this.acceptanceBrand = acceptanceBrand;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getIssuingICA() {
		return issuingICA;
	}
	public void setIssuingICA(String issuingICA) {
		this.issuingICA = issuingICA;
	}

	public Boolean getCrossBorder() {
		return crossBorder;
	}

	public void setCrossBorder(Boolean crossBorder) {
		this.crossBorder = crossBorder;
	}
}
