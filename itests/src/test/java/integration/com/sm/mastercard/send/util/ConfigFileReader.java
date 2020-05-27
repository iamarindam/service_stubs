package integration.com.sm.mastercard.send.util;

import com.mastercard.ap.core.platform.logging.logger.ZappLoggerFactory;
import com.mastercard.ap.core.platform.logging.logger.api.ZappLogger;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigFileReader {
    private static final ZappLogger log = ZappLoggerFactory.getLogger(ConfigFileReader.class, true);
    private Properties prop;
    private final String propertyFilePath= "src/test/resources/integrationTest.properties";

    public ConfigFileReader() {
        log.info("Reading properties file...");
        try {
            File file = Paths.get(propertyFilePath).toFile();
            FileReader reader = new FileReader(file);
            this.prop = new Properties();
            prop.load(reader);
        }catch (Exception ex){
            log.warn("exception occurred");
        }
        log.info ("Properties "+ propertyFilePath +"file loaded successfully");
    }

    public String getProperty(String propertyName) {
        String property = prop.getProperty(propertyName);
        if (property != null) return property;
        else throw new RuntimeException("Property" + propertyName + " is not specified in the properties file.");
    }

    public void setProperty(String propertyName, String value) {
        String property = prop.getProperty(propertyName);
        if (property != null)
            prop.setProperty(propertyName, value);
        else
            throw new RuntimeException("Property" + propertyName + " is not specified in the properties file.");
    }

    public String getTransferEligibilityUrl() {
        String property = prop.getProperty("base.domain")+ prop.getProperty("base.ip") + prop.getProperty("transfer.eligibility.url");
        if (property != null) return property;
        else throw new RuntimeException("Property TransferEligibilityUrl is not specified in the properties file.");
    }

    public String getTeSigUrl() {
        String property = prop.getProperty("base.domain")+ prop.getProperty("base.ip") + prop.getProperty("transfer.eligibility.signature.url");
        if (property != null) return property;
        else throw new RuntimeException("Property TransferEligibility Signature Url is not specified in the properties file.");
    }

    public String getPaymentsUrl() {
        String property = prop.getProperty("base.domain")+ prop.getProperty("base.ip") + prop.getProperty("payments.url");
        if (property != null) return property;
        else throw new RuntimeException("Property TransferEligibilityUrl is not specified in the properties file.");
    }

    public String getPaymentsSigUrl() {
        String property = prop.getProperty("base.domain")+ prop.getProperty("base.ip") + prop.getProperty("payments.signature.url");
        if (property != null) return property;
        else throw new RuntimeException("Property TransferEligibility Signature Url is not specified in the properties file.");
    }
}