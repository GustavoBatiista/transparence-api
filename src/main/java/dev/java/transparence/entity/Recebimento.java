package dev.java.transparence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "recebimento")
public class Recebimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao_recebimento", nullable = false)
    private String descricao;
    @Column(name = "valor_recebimento", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;
    @Column(name = "comprovante_url")
    private String comprovanteUrl;
    @ManyToOne
    @JoinColumn(name = "pessoa_cuidada_id", nullable = false)
    private PessoaCuidada pessoaCuidada;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;
    public Recebimento() {
    }

    public Recebimento(PessoaCuidada pessoaCuidada, Usuario usuario, Contrato contrato,
            String descricao, BigDecimal valor, LocalDate dataRecebimento) {
        this.pessoaCuidada = pessoaCuidada;
        this.usuario = usuario;
        this.contrato = contrato;
        this.descricao = descricao;
        this.valor = valor;
        this.dataRecebimento = dataRecebimento;
    }

    public Long getId() {
        return id;
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

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public String getComprovanteUrl() {
        return comprovanteUrl;
    }

    public void setComprovanteUrl(String comprovanteUrl) {
        this.comprovanteUrl = comprovanteUrl;
    }

    public PessoaCuidada getPessoaCuidada() {
        return pessoaCuidada;
    }

    public void setPessoaCuidada(PessoaCuidada pessoaCuidada) {
        this.pessoaCuidada = pessoaCuidada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Contrato getContrato() {
        return contrato;
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
        Recebimento other = (Recebimento) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Recebimento [id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", dataRecebimento=" + dataRecebimento
                + ", comprovanteUrl=" + comprovanteUrl + ", pessoaCuidada=" + pessoaCuidada + ", usuario=" + usuario
                + ", contrato=" + contrato + "]";
    }

}
