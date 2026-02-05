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

import dev.java.transparence.dto.RecebimentoRequestDTO;
import dev.java.transparence.dto.RecebimentoResponseDTO;
import dev.java.transparence.service.RecebimentoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "4 - Recebimento", description = "API para gerenciar recebimentos")
@RestController
@RequestMapping("/recebimentos")
public class RecebimentoController {

    private RecebimentoService recebimentoService;

    public RecebimentoController(RecebimentoService recebimentoService) {
        this.recebimentoService = recebimentoService;
    }

    @GetMapping
    public ResponseEntity<List<RecebimentoResponseDTO>> buscarTodosRecebimentos() {
        return ResponseEntity.status(HttpStatus.OK).body(recebimentoService.buscarTodosRecebimentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecebimentoResponseDTO> buscarRecebimentoPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recebimentoService.buscarRecebimentoPorId(id));
    }

    @PostMapping

    public ResponseEntity<RecebimentoResponseDTO> incluirRecebimento(@RequestBody RecebimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recebimentoService.incluirRecebimento(dto));
    }

    @PutMapping("/{id}")

    public ResponseEntity<RecebimentoResponseDTO> atualizarRecebimento(@PathVariable Long id,
            @RequestBody RecebimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recebimentoService.atualizarRecebimento(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRecebimento(@PathVariable Long id) {
        recebimentoService.excluirRecebimento(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
