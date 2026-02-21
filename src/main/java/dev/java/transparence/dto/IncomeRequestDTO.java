package dev.java.transparence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class IncomeRequestDTO {


    private Long contractId;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "value is required")
    private BigDecimal value;
    @NotBlank(message = "Income date is required")
    private LocalDate dataincome;

    public Long getcontractId() {
        return contractId;
    }

    public void setcontractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getvalue() {
        return value;
    }

    public void setvalue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getDataincome() {
        return dataincome;
    }

    public void setDataincome(LocalDate dataincome) {
        this.dataincome = dataincome;
    }

}
