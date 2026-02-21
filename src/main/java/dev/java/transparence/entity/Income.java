package dev.java.transparence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "income", indexes = {
        @Index(name = "idx_income_contract", columnList = "contract_id"),
        @Index(name = "idx_income_validation", columnList = "contract_id, data_income, value_income")
})
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description_income", nullable = false)
    private String description;
    @Column(name = "value_income", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;
    @Column(name = "data_income", nullable = false)
    private LocalDate dataincome;
    @Column(name = "receipt_url")
    private String receiptUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false, foreignKey = @ForeignKey(name = "fk_income_contract"))
    private Contract contract;

    public Income() {
    }

    public Income(Contract contract,
            String description, BigDecimal value, LocalDate dataincome) {
        this.contract = contract;
        this.description = description;
        this.value = value;
        this.dataincome = dataincome;
    }

    public Long getId() {
        return id;
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

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }
    public Contract getcontract() {
        return contract;
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
        Income other = (Income) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "income [id=" + id +
                ", description=" + description +
                ", value=" + value +
                ", dataincome=" + dataincome +
                ", receiptUrl=" + receiptUrl + "]";
    }
}
