package dev.java.transparence.controller;

import java.util.List;

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

import dev.java.transparence.dto.IncomeRequestDTO;
import dev.java.transparence.dto.IncomeResponseDTO;
import dev.java.transparence.service.IncomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "4 - income", description = "API for managing incomes")
@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> findAllIncome() {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.findAllIncome());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> findIncomeById(@PathVariable  Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.findIncomeById(id));
    }

    @PostMapping

    public ResponseEntity<IncomeResponseDTO> createIncome(@RequestBody @Valid IncomeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.createIncome(dto));
    }

    @PutMapping("/{id}")

    public ResponseEntity<IncomeResponseDTO> updateIncome(@PathVariable  Long id,
            @RequestBody @Valid IncomeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(incomeService.updateIncome(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable  Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
