package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    public boolean existsBycontract_IdAndDataexpenseAndvalue(
            Long contractId,
            LocalDate dataexpense,
            BigDecimal value);
}
