package com.sm.mastercard.send.config;

import com.sm.mastercard.send.constants.McSendConstants;
import com.sm.mastercard.send.filter.McSendFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	@Bean
	public FilterRegistrationBean<McSendFilter> loggingFilter(){
	    FilterRegistrationBean<McSendFilter> registrationBean = new FilterRegistrationBean<>();
	         
	    registrationBean.setFilter(new McSendFilter());
	    registrationBean.addUrlPatterns(McSendConstants.URL+McSendConstants.TRANSFER_ELIGIBILITY_URL+"/*");
		registrationBean.addUrlPatterns(McSendConstants.URL+McSendConstants.PAYMENTS_URL+"/*");
		
	    return registrationBean;    
	}
     

}
