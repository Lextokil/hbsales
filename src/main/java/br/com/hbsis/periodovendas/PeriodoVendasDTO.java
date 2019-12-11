package br.com.hbsis.periodovendas;

import br.com.hbsis.util.DateValidator;

import java.time.LocalDateTime;

public class PeriodoVendasDTO {

    private Long id;
    private String dataInicio;
    private String dataFinal;
    private String dataRetirada;
    private String descricao;
    private Long idFornecedor;

    public PeriodoVendasDTO() {
    }


    public PeriodoVendasDTO(Long id, String dataInicio, String dataFinal, String dataRetirada, String descricao, Long idFornecedor) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.dataRetirada = dataRetirada;
        this.descricao = descricao;
        this.idFornecedor = idFornecedor;
    }

    public static PeriodoVendasDTO of (PeriodoVendas periodoVendas){
        PeriodoVendasDTO periodoVendasDTO = new PeriodoVendasDTO(
                periodoVendas.getId(),
                DateValidator.convertDateToString(periodoVendas.getDataInicio()),
                DateValidator.convertDateToString(periodoVendas.getDataFinal()),
                DateValidator.convertDateToString(periodoVendas.getDataRetirada()),
                periodoVendas.getDescricao(),
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

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(String dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

}
