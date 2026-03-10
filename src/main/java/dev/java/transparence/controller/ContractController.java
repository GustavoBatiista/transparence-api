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

import dev.java.transparence.dto.ContractRequestDTO;
import dev.java.transparence.dto.ContractResponseDTO;
import dev.java.transparence.service.ContractService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contracts")
@Tag(name = "4 - Contracts", description = "API for managing contracts")
public class ContractController {

    private ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<ContractResponseDTO> createContract(@RequestBody @Valid ContractRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contractService.createContract(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> findContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.findContractById(id));
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<ContractResponseDTO> suspendContract(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.suspendContract(id));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<ContractResponseDTO> closeContract(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.closeContract(id));
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<ContractResponseDTO> reactivateContract(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.reactivateContract(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}