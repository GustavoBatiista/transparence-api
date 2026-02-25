package dev.java.transparence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponseDTO {

    private Long id;
    private String description;
    private BigDecimal value;
    private LocalDate data;
    private String receiptUrl;
    private Long dependentId;
    private Long userId;
    private Long contractId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public Long getDependentId() {
        return dependentId;
    }

    public void setdependentId(Long dependentId) {
        this.dependentId = dependentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setuserId(Long userId) {
        this.userId = userId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setcontractId(Long contractId) {
        this.contractId = contractId;
    }

}
