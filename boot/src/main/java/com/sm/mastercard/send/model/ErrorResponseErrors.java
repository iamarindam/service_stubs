package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseErrors {
	@NotNull
	@Valid
	@JsonProperty("Error")
	private List<ErrorList> error = new ArrayList<>();

	public List<ErrorList> getError()
	{
		return error;
	}

	public void setError(List<ErrorList> error)
	{
		this.error = error;
	}

}
