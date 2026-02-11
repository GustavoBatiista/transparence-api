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

import dev.java.transparence.dto.ContratoRequestDTO;
import dev.java.transparence.dto.ContratoResponseDTO;
import dev.java.transparence.service.ContratoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "3 - Contrato", description = "API para gerenciar contratos")
@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    public ResponseEntity<ContratoResponseDTO> incluirContrato(@RequestBody @Valid ContratoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratoService.incluirContrato(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> buscarContratoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contratoService.buscarContratoPorId(id));
    }

    @PutMapping("/{id}/suspender")
    public ResponseEntity<ContratoResponseDTO> suspenderContrato(@PathVariable Long id) {
        return ResponseEntity.ok(contratoService.suspenderContrato(id));
    }

    @PutMapping("/{id}/encerrar")
    public ResponseEntity<ContratoResponseDTO> encerrarContrato(@PathVariable Long id) {
        return ResponseEntity.ok(contratoService.encerrarContrato(id));
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ContratoResponseDTO> reativarContrato(@PathVariable Long id) {
        return ResponseEntity.ok(contratoService.reativarContrato(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirContrato(@PathVariable Long id) {
        contratoService.excluirContrato(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}