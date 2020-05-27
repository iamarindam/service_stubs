package com.sm.mastercard.send.constants;

public enum McSendEnum {

    SERVER_START_UP("<------- *** MasterCard-Send service started successfully *** ------->"),
    ERROR_MAPPER_START("** Error mapping initiated **"),
    ERROR_MAPPER_FINISH("** Error mapping completed **"),
    SIGNATURE_VER_FAILED("Signature verification failed"),
    SIGNATURE_VER_SUCCESS("Signature verification successful"),
    SIGNATURE_GEN_FAILED("Signature generation failed"),
    SIGNATURE_GEN_SUCCESS("Signature generation successful"),

    PAYMENTS_START("MC-Send Payments request initiated"),
    PAYMENTS_CORR_ID("Initiating MasterCard-Send Payments for correlationId :"),
    PAYMENTS_CONTROLLER_END("MasterCard-Send Payments request completed for correlationId : "),
    PAYMENTS_SIGNING_ENABLED("Signing for MC-Send Payments : "),
    PAYMENTS_ERROR_RESPONSE("Error response from MC Payments service"),
    PAYMENTS_SUCCESS_RESPONSE("Success response from MC Payments service"),
    PAYMENTS_RESPONSE_MSG("Processing response for MasterCard-Send Payment request with correlationId :"),
    PAYMENTS_SIGNATURE_GEN("Generate signature for Payments initiated."),

    TE_START("MC-Send Transfer Eligibility request initiated"),
    TE_CORR_ID("Initiating MasterCard-Send Transfer Eligibility request for correlationId :"),
    TE_CONTROLLER_END("MasterCard-Send Transfer Eligibility request completed for correlationId : "),
    TE_SIGNING_ENABLED("Signing for MC-Send Transfer Eligibility : "),
    TE_ERROR_RESPONSE("Error response from MC Transfer Eligibility service"),
    TE_SUCCESS_RESPONSE("Success response from MC Transfer Eligibility service"),
    TE_RESPONSE_MSG("Processing response for MasterCard-Send Transfer Eligibility request with correlationId :"),
    TE_SIGNATURE_GEN("Generate signature for Transfer Eligibility initiated.");

    private String log;

    McSendEnum(String log) {
        this.log = log;
    }

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }
    
}
