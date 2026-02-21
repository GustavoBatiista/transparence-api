package dev.java.transparence.service;


import dev.java.transparence.dto.DependentRequestDTO;
import dev.java.transparence.dto.DependentResponseDTO;
import dev.java.transparence.entity.Dependent;


public interface dependentService {

    public Dependent findDependentEntityById(Long id);

    public DependentResponseDTO createDependent(DependentRequestDTO dto);
    // TODO: separar DTO de criação e atualização futuramente 09/02/2026
    public DependentResponseDTO updateDependent(Long id, DependentRequestDTO dto);

    public void deleteDependent(Long id);

    public DependentResponseDTO findDependentById(Long id);
}
