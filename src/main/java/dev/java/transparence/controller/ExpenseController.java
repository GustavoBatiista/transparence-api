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

import dev.java.transparence.dto.ExpenseRequestDTO;
import dev.java.transparence.dto.ExpenseResponseDTO;
import dev.java.transparence.service.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/expenses")
@Tag(name = "6 - Expenses", description = "API for managing expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> buscarTodosexpenses() {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.buscarTodosexpenses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> findExpenseById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.findExpenseById(id));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @RequestBody @Valid ExpenseRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable Long id, @RequestBody @Valid ExpenseRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.updateExpense(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable  Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
