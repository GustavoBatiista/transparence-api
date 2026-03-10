package dev.java.transparence.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.java.transparence.dto.DependentRequestDTO;
import dev.java.transparence.dto.DependentResponseDTO;
import dev.java.transparence.service.DependentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Dependents", description = "API for managing dependents")
@RestController
@RequestMapping("/dependents")
public class DependentController {

    private DependentService dependentService;

    public DependentController(DependentService dependentService) {
        this.dependentService = dependentService;
    }

    @PostMapping
    public ResponseEntity<DependentResponseDTO> createDependent(@RequestBody @Valid DependentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dependentService.createDependent(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DependentResponseDTO> updateDependent(@PathVariable Long id,
            @RequestBody @Valid DependentRequestDTO dto) {
        return ResponseEntity.ok(dependentService.updateDependent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDependent(@PathVariable  Long id) {
        dependentService.deleteDependent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DependentResponseDTO> findDependentById(@PathVariable  Long id) {
        return ResponseEntity.ok(dependentService.findDependentById(id));
    }

}
