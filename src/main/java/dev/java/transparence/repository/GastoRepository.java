package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Gasto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
    public boolean existsByContrato_IdAndDataGastoAndValor(
            Long contratoId,
            LocalDate dataGasto,
            BigDecimal valor);
}
