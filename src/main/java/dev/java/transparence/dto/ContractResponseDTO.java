package dev.java.transparence.dto;


import java.time.LocalDate;

import dev.java.transparence.enums.ContractStatus;

public class ContractResponseDTO {

    private Long id;
    private Long userId;
    private Long dependentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

}
