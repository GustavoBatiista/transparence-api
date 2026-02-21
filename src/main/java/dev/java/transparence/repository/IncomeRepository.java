package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Income;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    public boolean existsBycontract_IdAndDataincomeAndvalue(
            Long contractId,
            LocalDate dataincome,
            BigDecimal value);
}
