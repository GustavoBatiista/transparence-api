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
    private LocalDate dataIncome;

    public Long getContractId() {
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getDataIncome() {
        return dataIncome;
    }

    public void setDataIncome(LocalDate dataIncome) {
        this.dataIncome = dataIncome;
    }

}
