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
    @NotBlank(message = "Adress is required")
    private String adress;
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

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
