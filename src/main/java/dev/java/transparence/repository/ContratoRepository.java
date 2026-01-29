package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Contrato;
import dev.java.transparence.enums.StatusContrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    public Boolean existsByUsuarioIdAndPessoaCuidadaIdAndStatus(
        Long usuarioId,
        Long pessoaCuidadaId,
        StatusContrato status
    );
}
