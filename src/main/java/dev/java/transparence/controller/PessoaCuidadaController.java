package dev.java.transparence.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.service.PessoaCuidadaService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    public ResponseEntity<PessoaCuidada> incluirPessoaCuidada(@RequestBody PessoaCuidada pessoaCuidada) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaCuidadaService.incluirPessoaCuidada(pessoaCuidada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaCuidada> atualizarPessoaCuidada(@PathVariable Long id,
            @RequestBody PessoaCuidada pessoaCuidada) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pessoaCuidadaService.atualizarPessoaCuidada(id, pessoaCuidada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPessoaCuidada(@PathVariable Long id) {
        pessoaCuidadaService.excluirPessoaCuidada(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaCuidada> buscarPessoaCuidadaPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaCuidadaService.buscarPessoaCuidadaPorId(id));
    }

}
