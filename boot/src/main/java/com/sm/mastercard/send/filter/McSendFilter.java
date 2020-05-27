package com.sm.mastercard.send.filter;

import com.sm.mastercard.send.constants.McSendConstants;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
@Component
public class McSendFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		 HttpServletRequest req = (HttpServletRequest) request;
	        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(req);
	        
	        String correlationId = UUID.randomUUID().toString();
	        mutableRequest.putHeader(McSendConstants.CORRELATION_ID, correlationId);
	        chain.doFilter(mutableRequest, response);
		
	}
}
