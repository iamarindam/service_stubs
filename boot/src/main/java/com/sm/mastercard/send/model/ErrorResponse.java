package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-01-31T17:51:40.482+05:30")

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	  @NotNull
      @Valid
	  @JsonProperty(value = "Errors", required = true)
	  private ErrorResponseErrors errors ;
	  public ErrorResponseErrors getErrors() {
		return errors;
		}
	  public void setErrors(ErrorResponseErrors errors) {
		this.errors = errors;
		}

}
