package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Dependent;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, Long> {

    boolean existsByCpf(String cpf);
}
