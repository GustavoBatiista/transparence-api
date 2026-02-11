package dev.java.transparence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class GastoRequestDTO {

    private Long contratoId;
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    @NotBlank(message = "Valor é obrigatório")
    private BigDecimal valor;
    @NotBlank(message = "Data de gasto é obrigatória")
    private LocalDate dataGasto;

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
