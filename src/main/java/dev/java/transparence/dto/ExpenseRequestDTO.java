package dev.java.transparence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class ExpenseRequestDTO {

    private Long contractId;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "value is required")
    private BigDecimal value;
    @NotBlank(message = "Expense date is required")
    private LocalDate dataExpense;

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

    public LocalDate getDataExpense() {
        return dataExpense;
    }

    public void setDataExpense(LocalDate dataExpense) {
        this.dataExpense = dataExpense;
    }

}
