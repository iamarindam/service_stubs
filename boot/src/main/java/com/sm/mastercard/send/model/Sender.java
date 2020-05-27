package com.sm.mastercard.send.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sender {
    @NotEmpty(message = "Account Uri cannot be empty")
    @NotBlank(message = "Account Uri cannot be blank")
    @JsonProperty("accountUri")
    private String accountUri;

    @NotNull(message = "Address cannot be null")
    @Valid
    @JsonProperty("address")
    private Address address;

    @JsonProperty("countryOfBirth")
    private String countryOfBirth;

    @JsonProperty("dateOfBirth")
    @Pattern(regexp ="^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "birthDate should be of proper yyyy-MM-dd type only.")
    private String dateOfBirth;

    @JsonProperty("digitalAccountReferenceNumber")
    private String digitalAccountReferenceNumber;

    @Size(max = 254, message = "Email size cannot exceed 254")
    @JsonProperty("email")
    private String email;

    @NotEmpty(message = "Name cannot be empty")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 70, message = "Name must be between 1 and 70")
    @JsonProperty("name")
    private String name;

    @JsonProperty("nationality")
    private String nationality;

    @Size(max = 20, message = "Phone size cannot exceed 20")
    @JsonProperty("phone")
    private String phone;

    public String getAccountUri() {
        return accountUri;
    }

    public void setAccountUri(String accountUri) {
        this.accountUri = accountUri;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDigitalAccountReferenceNumber() {
        return digitalAccountReferenceNumber;
    }

    public void setDigitalAccountReferenceNumber(String digitalAccountReferenceNumber) {
        this.digitalAccountReferenceNumber = digitalAccountReferenceNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
