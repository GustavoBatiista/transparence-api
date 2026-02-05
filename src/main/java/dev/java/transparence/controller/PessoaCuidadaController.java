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

import dev.java.transparence.dto.PessoaCuidadaRequestDTO;
import dev.java.transparence.dto.PessoaCuidadaResponseDTO;
import dev.java.transparence.service.PessoaCuidadaService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2 - Pessoa Cuidada", description = "API para gerenciar pessoas cuidadas")
@RestController
@RequestMapping("/pessoas-cuidadas")
public class PessoaCuidadaController {

    private PessoaCuidadaService pessoaCuidadaService;

    public PessoaCuidadaController(PessoaCuidadaService pessoaCuidadaService) {
        this.pessoaCuidadaService = pessoaCuidadaService;
    }

    @PostMapping
    public ResponseEntity<PessoaCuidadaResponseDTO> incluirPessoaCuidada(@RequestBody PessoaCuidadaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaCuidadaService.incluirPessoaCuidada(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaCuidadaResponseDTO> atualizarPessoaCuidada(@PathVariable Long id,
            @RequestBody PessoaCuidadaRequestDTO dto) {
        return ResponseEntity.ok(pessoaCuidadaService.atualizarPessoaCuidada(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPessoaCuidada(@PathVariable Long id) {
        pessoaCuidadaService.excluirPessoaCuidada(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaCuidadaResponseDTO> buscarPessoaCuidadaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaCuidadaService.buscarPessoaCuidadaPorId(id));
    }

}
