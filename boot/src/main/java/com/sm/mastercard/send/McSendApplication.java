package com.sm.mastercard.send;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;
import com.sm.mastercard.send.constants.McSendEnum;

@SpringBootApplication
@ImportResource({"classpath:/spring/zapp-rest-logging-interceptors.xml","classpath:/spring/zapp-logging-config.xml"})
public class McSendApplication {

	private static final ZappLogger LOG = ZappLoggerFactory.getLogger(McSendApplication.class,true);
	public static void main(String[] args) {
		SpringApplication.run(McSendApplication.class);
		LOG.info(McSendEnum.SERVER_START_UP.getLog());
	}
}
