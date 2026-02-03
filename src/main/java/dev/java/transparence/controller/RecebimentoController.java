package dev.java.transparence.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
import dev.java.transparence.entity.Recebimento;
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
    public ResponseEntity<List<Recebimento>> buscarTodosRecebimentos() {
        return ResponseEntity.status(HttpStatus.OK).body(recebimentoService.buscarTodosRecebimentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recebimento> buscarRecebimentoPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recebimentoService.buscarRecebimentoPorId(id));
    }

    @PostMapping
    public ResponseEntity<Recebimento> incluirRecebimento(@RequestParam Long pessoaCuidadaId,
            @RequestParam Long usuarioId, @RequestParam Long contratoId, @RequestParam String descricao,
            @RequestParam BigDecimal valor, @RequestParam LocalDate dataRecebimento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recebimentoService.incluirRecebimento(pessoaCuidadaId,
                usuarioId, contratoId, descricao, valor, dataRecebimento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recebimento> atualizarRecebimento(@PathVariable Long id, @RequestParam String descricao,
            @RequestParam BigDecimal valor, @RequestParam LocalDate dataRecebimento) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recebimentoService.atualizarRecebimento(id, descricao, valor, dataRecebimento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRecebimento(@PathVariable Long id) {
        recebimentoService.excluirRecebimento(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
