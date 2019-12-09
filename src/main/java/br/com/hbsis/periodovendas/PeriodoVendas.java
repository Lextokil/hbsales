package br.com.hbsis.periodovendas;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "periodo_vendas")
public class PeriodoVendas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_inicio")
    private Date dataInicio;

    @Column(name = "data_final")
    private  Date dataFinal;

    @Column(name = "data_retirada")
    private Date dataRetirada;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;

    public PeriodoVendas() {
    }

    public PeriodoVendas(Date dataInicio, Date dataFinal, Date dataRetirada, Fornecedor fornecedor) {
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.dataRetirada = dataRetirada;
        this.fornecedor = fornecedor;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Date dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}
