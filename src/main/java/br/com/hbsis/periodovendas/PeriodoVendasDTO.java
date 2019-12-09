package br.com.hbsis.periodovendas;

import java.util.Date;

public class PeriodoVendasDTO {

    private Long id;
    private Date dataInicio;
    private Date dataFinal;
    private Date dataRetirada;
    private Long idFornecedor;

    public PeriodoVendasDTO() {
    }



    public PeriodoVendasDTO(Long id, Date dataInicio, Date dataFinal, Date dataRetirada, Long idFornecedor) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.dataRetirada = dataRetirada;
        this.idFornecedor = idFornecedor;
    }

    public static PeriodoVendasDTO of (PeriodoVendas periodoVendas){
        PeriodoVendasDTO periodoVendasDTO = new PeriodoVendasDTO(
                periodoVendas.getId(),
                periodoVendas.getDataInicio(),
                periodoVendas.getDataFinal(),
                periodoVendas.getDataRetirada(),
                periodoVendas.getFornecedor().getId()
        );
        return periodoVendasDTO;
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

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }
}
