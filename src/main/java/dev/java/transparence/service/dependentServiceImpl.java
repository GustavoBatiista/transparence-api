package dev.java.transparence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.DependentRequestDTO;
import dev.java.transparence.dto.DependentResponseDTO;
import dev.java.transparence.entity.Dependent;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.DependentRepository;

@Service
public class dependentServiceImpl implements dependentService {

    private static final Logger log = LoggerFactory.getLogger(dependentServiceImpl.class);

    private DependentRepository dependentRepository;

    public dependentServiceImpl(DependentRepository dependentRepository) {
        this.dependentRepository = dependentRepository;
    }

    @Override
    public DependentResponseDTO createDependent(DependentRequestDTO dto) {

        log.debug("Starting dependent creation.");

        if (dependentRepository.existsByCpf(dto.getCpf())) {
            log.warn("Attempt to create a dependent with an existing CPF.");
            throw new BusinessException("CPF already registered");
        }

        Dependent dependent = new Dependent(
                dto.getCpf(),
                dto.getName(),
                dto.getphone(),
                dto.getadress(),
                dto.getcity(),
                dto.getstate(),
                dto.getzipCode());

        Dependent save = dependentRepository.save(dependent);

        log.info("Dependent created successfully. dependentId={}", save.getId());

        return toResponseDTO(save);
    }

    // TODO: separar DTO de criação e atualização futuramente 09/02/2026
    @Override
    public DependentResponseDTO updateDependent(Long id, DependentRequestDTO dto) {

        log.info("Starting dependent update. dependentId={}", id);

        Dependent existente = findDependentEntityById(id);

        existente.setName(dto.getName());
        existente.setPhone(dto.getphone());
        existente.setAdress(dto.getadress());
        existente.setCity(dto.getcity());
        existente.setState(dto.getstate());
        existente.setZipCode(dto.getzipCode());

        Dependent save = dependentRepository.save(existente);

        log.info("Dependent updated successfully. dependentId={}", save.getId());

        return toResponseDTO(save);
    }

    @Override
    public void deleteDependent(Long id) {

        log.info("Starting dependent deletion. dependentId={}", id);

        if (!dependentRepository.existsById(id)) {
            log.warn("Attempt to delete a non-existing dependent. dependentId={}", id);
            throw new ResourceNotFoundException("Dependent not found");
        }

        dependentRepository.deleteById(id);

        log.info("Dependent deleted successfully. dependentId={}", id);
    }

    @Override
    public Dependent findDependentEntityById(Long id) {

        log.debug("Finding dependent in database. dependentId={}", id);

        return dependentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dependent not found"));
    }

    @Override
    public DependentResponseDTO findDependentById(Long id) {

        log.info("Finding dependent by id. dependentId={}", id);

        return toResponseDTO(findDependentEntityById(id));
    }

    private DependentResponseDTO toResponseDTO(Dependent dependent) {

        log.debug("Converting dependent to DTO. dependentId={}", dependent.getId());

        DependentResponseDTO dto = new DependentResponseDTO();

        dto.setId(dependent.getId());
        dto.setCpf(dependent.getCpf());
        dto.setname(dependent.getName());
        dto.setphone(dependent.getPhone());
        dto.setadress(dependent.getAdress());
        dto.setcity(dependent.getCity());
        dto.setstate(dependent.getState());
        dto.setzipCode(dependent.getZipCode());

        return dto;
    }
}