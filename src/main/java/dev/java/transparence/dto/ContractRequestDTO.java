package dev.java.transparence.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class ContractRequestDTO {
    private Long userId;
    private Long dependentId;
    @NotBlank(message = "Start date is required")
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getuserId() {
        return userId;
    }

    public void setuserId(Long userId) {
        this.userId = userId;
    }

    public Long getdependentId() {
        return dependentId;
    }

    public void setdependentId(Long dependentId) {
        this.dependentId = dependentId;
    }

    public LocalDate getstartDate() {
        return startDate;
    }

    public void setstartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getendDate() {
        return endDate;
    }

    public void setendDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
