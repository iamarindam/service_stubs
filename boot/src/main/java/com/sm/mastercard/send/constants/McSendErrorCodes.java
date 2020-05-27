package com.sm.mastercard.send.constants;

/*
 *
 * @Author: Apeksha Ubhayakar
 * @Date: 28-11-2019
 *
 */

public final class McSendErrorCodes {
    private McSendErrorCodes() { }

    public static final String INTERNAL_TECHNICAL_ERROR = "SMGR-9999";
    public static final String INTERNAL_TIMEOUT_ERROR = "TOUT-SEND-9999";
    public static final String INTERNAL_SECURITY_TIMEOUT_ERROR = "TOUT-SEC-9999";

    public static final String STRUCTURAL_ERROR = "STVL-9999";
    public static final String TECHNICAL_ERROR = "TECH-9999";
    public static final String TIMEOUT_ERROR = "TOUT-9999";
    public static final String DECLINE = "DECLINE";
    public static final String RESOURCE_UNKNOWN = "RESOURCE_UNKNOWN";
    public static final String RESOURCE_ERROR = "RESOURCE_ERROR";

    public static final String ERROR_CODE_110503 = "110503";
    public static final String ERROR_CODE_130004 = "130004";
}
