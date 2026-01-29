package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Recebimento;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface RecebimentoRepository extends JpaRepository<Recebimento, Long> {

    public Boolean existsLancamentoRecebimento(
        Long pessoaCuidadaId,
        Long usuarioId,
        LocalDate dataRecebimento,
        BigDecimal valor
    );
}
