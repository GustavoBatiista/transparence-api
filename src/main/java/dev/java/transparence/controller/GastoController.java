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

import dev.java.transparence.entity.Gasto;
import dev.java.transparence.service.GastoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "5 - Gasto", description = "API para gerenciar gastos")
@RestController
@RequestMapping("/gastos")
public class GastoController {

    private GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping
    public ResponseEntity<List<Gasto>> buscarTodosGastos() {
        return ResponseEntity.status(HttpStatus.OK).body(gastoService.buscarTodosGastos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gasto> buscarGastoPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(gastoService.buscarGastoPorId(id));
    }

    @PostMapping
    public ResponseEntity<Gasto> incluirGasto(@RequestParam Long pessoaCuidadaId, @RequestParam Long usuarioId,
            @RequestParam Long contratoId, @RequestParam String descricao, @RequestParam BigDecimal valor,
            @RequestParam LocalDate dataGasto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gastoService.incluirGasto(pessoaCuidadaId, usuarioId, contratoId, descricao, valor, dataGasto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gasto> atualizarGasto(@PathVariable Long id, @RequestParam String descricao,
            @RequestParam BigDecimal valor,
            @RequestParam LocalDate dataGasto) {
        return ResponseEntity.status(HttpStatus.OK).body(gastoService.atualizarGasto(id, descricao, valor, dataGasto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirGasto(@PathVariable Long id) {
        gastoService.excluirGasto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
