package com.sm.mastercard.send.constants;

/*
 *
 * @Author: Apeksha Ubhayakar
 * @Date: 28-11-2019
 *
 */

public final class McSendErrorDescription {
    private McSendErrorDescription() { }

    public static final String INTERNAL_TIMEOUT_ERROR = "SEND has timed out";
    public static final String INTERNAL_SECURITY_TIMEOUT_ERROR = "Security times out";

    public static final String STRUCTURAL_ERROR = "Incorrect Format";
    public static final String TECHNICAL_ERROR = "Technical Issues Encountered";
    public static final String TIMEOUT_ERROR = "Request was timed out";

    public static final String DESC_110502 = "No default account is defined for the consumer";
    public static final String DESC_110503 = "Account not eligible";
    public static final String DESC_110504 = "Card type is not supported for merchant";
    public static final String DESC_110505 = "Invalid currency";

    public static final String DESC_110516 = "Country not supported for merchant";
    public static final String DESC_110537 = "Country is not supported for the merchant";
    public static final String DESC_130001= "Card declined";
    public static final String DESC_130002 = "Fraud detected";
    public static final String DESC_130003 = "Card expired";

    public static final String DESC_130004 = "Per transaction maximum amount limit reached. Additional details will be provided in some scenarios";
    public static final String DESC_130005 = "Partner has exceeded the daily limit configured in the system";
    public static final String DESC_130006 = "Transaction Limit is less than the minimum configured for the partner";
    public static final String DESC_130007 = "Consumer has exceeded the monthly limit configured in the system";
    public static final String DESC_130008 = "Partner has exceeded the cycle limit configured in the system";
}
