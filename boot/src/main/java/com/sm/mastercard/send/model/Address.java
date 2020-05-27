package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Size(min = 1, max = 25 ,message = "City must be between 1 and 25")
    @JsonProperty("city")
    private String city;

    @NotEmpty(message = "Country cannot be empty")
    @NotBlank(message = "Country cannot be blank")
    @JsonProperty("country")
    private String country;

    @Size(min = 1, max = 10 ,message = "PostalCode size must be between 1 and 10")
    @JsonProperty("postalCode")
    private String postalCode;

    @Size(min = 2, max = 3 ,message = "State size must be between 2 and 3")
    @JsonProperty("state")
    private String state;

    @NotEmpty(message = "Street cannot be empty")
    @NotBlank(message = "Street cannot be blank")
    @Size(min = 1, max = 50 ,message = "Street must be between 1 and 50")
    @JsonProperty("street")
    private String street;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
