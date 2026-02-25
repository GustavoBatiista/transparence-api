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
    @NotBlank(message = "Adress is required")
    private String adress;
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

    public void setname(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getadress() {
        return adress;
    }

    public void setadress(String adress) {
        this.adress = adress;
    }

    public String getcity() {
        return city;
    }

    public void setcity(String city) {
        this.city = city;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }

    public String getzipCode() {
        return zipCode;
    }

    public void setzipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
