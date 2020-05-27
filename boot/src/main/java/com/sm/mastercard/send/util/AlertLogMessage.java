package com.sm.mastercard.send.util;

import com.sm.mastercard.send.model.ErrorAlertMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlertLogMessage {
	private static String pidf;
	private static String servName;
	private static String operName;
	
	@Value( "${send.error.alert.productidentifier}" )
	public void setProductIdentifier(String pidf) {
		AlertLogMessage.pidf = pidf;
	}  
	
	@Value( "${send.error.alert.servicename}" )
	public void setServiceName(String servName) {
		AlertLogMessage.servName = servName;
	}  
	
	@Value( "${send.error.alert.operationname}" )
	public void setOperationName(String operName) {
		AlertLogMessage.operName = operName;
	}  
	

	public static String getErrorAlertMessage(ErrorAlertMessage alertMsg){
		StringBuilder sb = new StringBuilder("EMS");
		sb.append(" ");
		sb.append(pidf);
		sb.append(" ");
		sb.append(alertMsg.getErrCode());
		sb.append(" ");
		sb.append(servName);
		sb.append(" ");
		sb.append(operName);
		sb.append(" ");
		sb.append(alertMsg.getCorrId());
		sb.append(" APP_MESSAGE ");
		sb.append(alertMsg.getErrMsgLength());
		sb.append(" | SUMMARY | ");
		if(null != alertMsg.getParticipantId()) {
			sb.append(alertMsg.getParticipantId());
			sb.append(" | ");
		}
		if(null != alertMsg.getTxnId()) {
			sb.append(alertMsg.getTxnId());
		}

		sb.append(" | ERROR_TRACE | ");

		if(null != alertMsg.getErrStackStrace()) {
			sb.append(" ");
			sb.append(alertMsg.getErrStackStrace());
		}
		
		return sb.toString();
	}

}
