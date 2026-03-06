package dev.java.transparence.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

    
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "password must be between 8 and 20 characters")
    private String password;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{11}", message = "phone must have at least 11 digits")
    private String phone;
    @NotBlank(message = "Addresss is required")
    private String address;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "State is required")
    @Pattern(regexp = "(?i)[a-z]{2}", message = "state must have at least 2 words")
    private String state;
    @NotBlank(message = "ZipCode is required")
    @Pattern(regexp = "\\d{8}", message = "zipCode must have at least 8 digits")
    private String zipCode;
    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "\\d{11}", message = "CPF must have at least 11 digits")
    private String cpf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
