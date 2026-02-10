package dev.java.transparence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "gasto", indexes = {
        @Index(name = "idx_gasto_contrato", columnList = "contrato_id"),
        @Index(name = "idx_gasto_validacao", columnList = "contrato_id, data_gasto, valor_gasto")
})
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao_gasto", nullable = false)
    private String descricao;
    @Column(name = "valor_gasto", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "data_gasto", nullable = false)
    private LocalDate dataGasto;
    @Column(name = "comprovante_url")
    private String comprovanteUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gasto_contrato"))
    private Contrato contrato;

    public Gasto() {
    }

    public Gasto(Contrato contrato,
            String descricao, BigDecimal valor, LocalDate dataGasto) {
        this.contrato = contrato;
        this.descricao = descricao;
        this.valor = valor;
        this.dataGasto = dataGasto;
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

    public LocalDate getDataGasto() {
        return dataGasto;
    }

    public void setDataGasto(LocalDate dataGasto) {
        this.dataGasto = dataGasto;
    }

    public String getComprovanteUrl() {
        return comprovanteUrl;
    }

    public void setComprovanteUrl(String comprovanteUrl) {
        this.comprovanteUrl = comprovanteUrl;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
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
        Gasto other = (Gasto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Gasto [id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", dataGasto=" + dataGasto
                + ", comprovanteUrl=" + comprovanteUrl + "]";
    }

}
