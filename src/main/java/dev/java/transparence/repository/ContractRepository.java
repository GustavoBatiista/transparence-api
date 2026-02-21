package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Contract;
import dev.java.transparence.enums.ContractStatus;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    public boolean existsByuser_IdAnddependent_IdAndStatus(
        Long userId,
        Long dependentId,
        ContractStatus status
    );
}
