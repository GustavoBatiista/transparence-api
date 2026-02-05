package dev.java.transparence.dto;

import java.time.LocalDate;

public class ContratoRequestDTO {
    private Long usuarioId;
    private Long pessoaCuidadaId;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getPessoaCuidadaId() {
        return pessoaCuidadaId;
    }

    public void setPessoaCuidadaId(Long pessoaCuidadaId) {
        this.pessoaCuidadaId = pessoaCuidadaId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

}
