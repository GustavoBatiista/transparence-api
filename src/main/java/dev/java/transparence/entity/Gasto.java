package dev.java.transparence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "gasto")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao_gasto", nullable = false)
    private String descricao;
    @Column(name = "valor_gasto", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "data_gasto", nullable = false)
    private LocalDate data;
    @Column(name = "comprovante_url")
    private String comprovanteUrl;
    @ManyToOne
    @JoinColumn(name = "pessoa_cuidada_id", nullable = false)
    private PessoaCuidada pessoaCuidada;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Gasto() {
    }

    public Gasto(PessoaCuidada pessoaCuidada, Usuario usuario,
        String descricao, BigDecimal valor, LocalDate dataGasto) {
        this.pessoaCuidada = pessoaCuidada;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.data = dataGasto;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getComprovanteUrl() {
        return comprovanteUrl;
    }

    public void setComprovanteUrl(String comprovanteUrl) {
        this.comprovanteUrl = comprovanteUrl;
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
        return "Gasto [id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", data=" + data
                + ", comprovanteUrl=" + comprovanteUrl + "]";
    }

}
