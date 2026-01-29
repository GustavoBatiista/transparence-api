package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.PessoaCuidada;

@Repository
public interface PessoaCuidadaRepository extends JpaRepository<PessoaCuidada, Long> {

    boolean existsByCpf(String cpf);
}
