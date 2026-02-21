package dev.java.transparence.entity;

import java.time.LocalDate;

import dev.java.transparence.enums.ContractStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contract", indexes = {
        @Index(name = "idx_contract_user", columnList = "user_id"),
        @Index(name = "idx_contract_dependent", columnList = "dependent_id"),
        @Index(name = "idx_contract_validation", columnList = "dependent_id, user_id,status")
})
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contract_user"))
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contract_dependent"))
    private Dependent dependent;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    public Contract() {
    }

    public Contract(User user, Dependent dependent, LocalDate startDate) {
        this.user = user;
        this.dependent = dependent;
        this.startDate = startDate;
        this.status = ContractStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public User getuser() {
        return user;
    }

    public void setuser(User user) {
        this.user = user;
    }

    public Dependent getdependent() {
        return dependent;
    }

    public void setdependent(Dependent dependent) {
        this.dependent = dependent;
    }

    public Long getdependentId() {
        return dependent.getId();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contract other = (Contract) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "contract [id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status + "]";
    }

}
