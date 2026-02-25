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
@Table(name = "expense", indexes = {
        @Index(name = "idx_expense_contract", columnList = "contract_id"),
        @Index(name = "idx_expense_validation", columnList = "contract_id, data_expense, value_expense")
})
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description_expense", nullable = false)
    private String description;
    @Column(name = "value_expense", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;
    @Column(name = "data_expense", nullable = false)
    private LocalDate dataExpense;
    @Column(name = "receipt_url")
    private String receiptUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_contract"))
    private Contract contract;

    public Expense() {
    }

    public Expense(Contract contract,
            String description, BigDecimal value, LocalDate dataExpense) {
        this.contract = contract;
        this.description = description;
        this.value = value;
        this.dataExpense = dataExpense;
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

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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
        Expense other = (Expense) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "expense [id=" + id + ", description=" + description + ", value=" + value + ", dataExpense=" + dataExpense
                + ", receiptUrl=" + receiptUrl + "]";
    }

}
