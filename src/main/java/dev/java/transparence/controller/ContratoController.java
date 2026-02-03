package dev.java.transparence.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.java.transparence.entity.Contrato;
import dev.java.transparence.service.ContratoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "3 - Contrato", description = "API para gerenciar contratos")
@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    public ResponseEntity<Contrato> incluirContrato(@RequestParam Long usuarioId,
            @RequestParam Long pessoaCuidadaId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratoService.incluirContrato(usuarioId, pessoaCuidadaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> buscarContratoPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(contratoService.buscarContratoPorId(id));
    }

    @PutMapping("/{id}/suspender")
    public ResponseEntity<Contrato> suspenderContrato(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(contratoService.suspenderContrato(id));
    }

    @PutMapping("/{id}/encerrar")
    public ResponseEntity<Contrato> encerrarContrato(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(contratoService.encerrarContrato(id));
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<Contrato> reativarContrato(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(contratoService.reativarContrato(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirContrato(@PathVariable Long id) {
        contratoService.excluirContrato(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}