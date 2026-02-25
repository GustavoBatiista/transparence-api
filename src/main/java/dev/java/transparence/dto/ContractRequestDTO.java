package dev.java.transparence.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class ContractRequestDTO {
    private Long userId;
    private Long dependentId;
    @NotBlank(message = "Start date is required")
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDependentId() {
        return dependentId;
    }

    public void setDependentId(Long dependentId) {
        this.dependentId = dependentId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
