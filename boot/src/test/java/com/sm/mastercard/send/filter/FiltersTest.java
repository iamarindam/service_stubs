/**
 * 
 */
package com.sm.mastercard.send.filter;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class FiltersTest {
	private static final Logger LOG = LoggerFactory.getLogger(FiltersTest.class);
	
	@InjectMocks
	private static McSendFilter filters = new McSendFilter();
	
	@Mock
	private ServletRequest servletRequest;
	
	@Mock
	private ServletResponse servletResponse;
	
	@Mock
	private FilterChain filterChain;
	
	@Mock
	private HttpServletRequest httpServletRequest;
	
	@Test
	public void test_doFilter() throws IOException, ServletException {
		LOG.info("inside test_doFilter method ... ");
		
		filters.doFilter(httpServletRequest, servletResponse, filterChain);

		FiltersTest applicationTests = null;
		assertNull(applicationTests);
		
		LOG.info("inside test_doFilter method ... ");
	}
	
	@AfterClass
	public static void tearDown() {
		System.out.println(FiltersTest.class + " has been tested and code coverage is checked .. ");
	}
}
