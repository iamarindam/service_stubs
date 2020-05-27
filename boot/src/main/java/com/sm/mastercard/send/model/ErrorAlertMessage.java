package com.sm.mastercard.send.model;

public class ErrorAlertMessage {

	private String errCode;
	private String corrId;
	private String errMsgLength;
	private String participantId;
	private String txnId;
	private String fieldName;
	private String errStackStrace;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getCorrId() {
		return corrId;
	}

	public void setCorrId(String corrId) {
		this.corrId = corrId;
	}

	public String getErrMsgLength() {
		return errMsgLength;
	}

	public void setErrMsgLength(String errMsgLength) {
		this.errMsgLength = errMsgLength;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getErrStackStrace() {
		return errStackStrace;
	}

	public void setErrStackStrace(String errStackStrace) {
		this.errStackStrace = errStackStrace;
	}

}
