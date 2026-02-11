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

import dev.java.transparence.dto.GastoRequestDTO;
import dev.java.transparence.dto.GastoResponseDTO;
import dev.java.transparence.service.GastoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "5 - Gasto", description = "API para gerenciar gastos")
@RestController
@RequestMapping("/gastos")
public class GastoController {

    private GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping
    public ResponseEntity<List<GastoResponseDTO>> buscarTodosGastos() {
        return ResponseEntity.status(HttpStatus.OK).body(gastoService.buscarTodosGastos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> buscarGastoPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(gastoService.buscarGastoPorId(id));
    }

    @PostMapping
    public ResponseEntity<GastoResponseDTO> incluirGasto(
            @RequestBody @Valid GastoRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gastoService.incluirGasto(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> atualizarGasto(@PathVariable Long id, @RequestBody @Valid GastoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(gastoService.atualizarGasto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirGasto(@PathVariable  Long id) {
        gastoService.excluirGasto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
