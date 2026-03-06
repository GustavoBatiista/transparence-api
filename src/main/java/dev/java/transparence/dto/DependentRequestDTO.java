package dev.java.transparence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DependentRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    private String cpf;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{11}", message = "phone deve ter 11 dígitos")
    private String phone;
    @NotBlank(message = "Addresss is required")
    private String address;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "State is required")
    @Pattern(regexp = "(?i)[a-z]{2}", message = "state deve ter 2 letras")
    private String state;
    @NotBlank(message = "ZipCode is required")
    @Pattern(regexp = "\\d{8}", message = "zipCode deve ter 8 dígitos")
    private String zipCode;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
