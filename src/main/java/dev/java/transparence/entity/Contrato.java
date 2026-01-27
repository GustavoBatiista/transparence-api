package dev.java.transparence.entity;

import java.time.LocalDate;

import dev.java.transparence.enums.StatusContrato;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "contrato")
public class Contrato  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "pessoa_cuidada_id", nullable = false)
    private PessoaCuidada pessoaCuidada;
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;
    @Column(name = "data_fim")
    private LocalDate dataFim;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusContrato status;

    public Contrato() {
    }

    public Contrato(Usuario usuario, PessoaCuidada pessoaCuidada, LocalDate dataInicio) {
        this.usuario = usuario;
        this.pessoaCuidada = pessoaCuidada;
        this.dataInicio = dataInicio;
        this.status = StatusContrato.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PessoaCuidada getPessoaCuidada() {
        return pessoaCuidada;
    }

    public void setPessoaCuidada(PessoaCuidada pessoaCuidada) {
        this.pessoaCuidada = pessoaCuidada;
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

    public StatusContrato getStatus() {
        return status;
    }

    public void setStatus(StatusContrato status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contrato other = (Contrato) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Contrato [id=" + id + ", usuario=" + usuario + ", pessoaCuidada=" + pessoaCuidada + ", dataInicio="
                + dataInicio + ", dataFim=" + dataFim + ", status=" + status + "]";
    }

}
