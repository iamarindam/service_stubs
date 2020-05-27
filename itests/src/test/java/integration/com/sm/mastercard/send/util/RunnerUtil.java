package integration.com.sm.mastercard.send.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.sm.mastercard.send.constants.McSendConstants;
import integration.com.sm.mastercard.send.config.ApplicationTestContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages test CONTEXT and REST API invocation.
 *
 */
public class RunnerUtil {

  private ApplicationTestContext CONTEXT = ApplicationTestContext.CONTEXT;

  protected ApplicationTestContext testContext() {
    return CONTEXT;
  }

  protected ConfigFileReader configFileReader = new ConfigFileReader();

  protected void executePost(String apiPath) {
    executePost(apiPath, null, null,null);
  }

  protected void executePost(String apiPath, Map<String, Object> pathParams) {
    executePost(apiPath, pathParams, null,null);
  }

  protected void executePostWithHeaders(String apiPath, Map<String, Object> headers) {
    executePost(apiPath, null, null,headers);
  }

  protected void executePost(String apiPath, Map<String, Object> pathParams, Map<String, String> queryParamas,Map<String,Object> headerParams) {
    final RequestSpecification request = CONTEXT.getRequest();
    final Object payload = CONTEXT.getPayload();

    setPayload(request, payload);
    setQueryParams(pathParams, request);
    setPathParams(queryParamas, request);
    setHeaderParams(headerParams,request);

    Response response = request.accept(ContentType.JSON)
      .log()
      .all()
      .post(apiPath);

    logResponse(response);
    CONTEXT.setResponse(response);
  }


  private void logResponse(Response response) {
    response.then()
      .log()
      .all();
  }

  private void setPathParams(Map<String, String> queryParamas, RequestSpecification request) {
    if (null != queryParamas) {
      request.queryParams(queryParamas);
    }
  }

  private void setHeaderParams(Map<String, Object> headerParams, RequestSpecification request) {
    if (null != headerParams) {
      request.headers(headerParams);
    }
  }

  private void setQueryParams(Map<String, Object> pathParams, RequestSpecification request) {
    if (null != pathParams) {
      request.pathParams(pathParams);
    }
  }

  private void setPayload(RequestSpecification request, Object payload) {
    if (null != payload) {
      request.contentType(ContentType.JSON)
        .body(payload);
    }
  }

  public Map<String,Object> setHeaders(String signature){
    Map<String, Object> headerParams = new HashMap<>();
    headerParams.put(McSendConstants.X_PARTICIPANT_ID, configFileReader.getProperty("test.data.participantId"));
    headerParams.put(McSendConstants.X_BUSINESS_MESSAGE_IDENTIFIER, configFileReader.getProperty("test.data.businessMsgId"));
    headerParams.put(McSendConstants.X_ENCRYPTED, configFileReader.getProperty("test.data.encrypted"));
    headerParams.put(McSendConstants.X_REPEAT_FLAG, configFileReader.getProperty("test.data.repeat"));
    headerParams.put(McSendConstants.X_JWS_SIGNATURE,signature);
    return headerParams;
  }

  @Bean
  public static ObjectMapper objectMapper() {
    JavaTimeModule module = new JavaTimeModule();
    LocalDateTimeDeserializer localDateTimeDeserializer =  new
            LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
    ObjectMapper objectMapperObj = Jackson2ObjectMapperBuilder.json()
            .modules(module)
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    return objectMapperObj ;
  }
}
