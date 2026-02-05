package dev.java.transparence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GastoRequestDTO {

    private Long id;
    private Long pessoaCuidadaId;
    private Long usuarioId;
    private Long contratoId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataGasto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPessoaCuidadaId() {
        return pessoaCuidadaId;
    }

    public void setPessoaCuidadaId(Long pessoaCuidadaId) {
        this.pessoaCuidadaId = pessoaCuidadaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getContratoId() {
        return contratoId;
    }

    public void setContratoId(Long contratoId) {
        this.contratoId = contratoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataGasto() {
        return dataGasto;
    }

    public void setDataGasto(LocalDate dataGasto) {
        this.dataGasto = dataGasto;
    }

}
