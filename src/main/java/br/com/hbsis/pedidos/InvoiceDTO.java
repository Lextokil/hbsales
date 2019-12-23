package br.com.hbsis.pedidos;

import br.com.hbsis.itempedido.InvoiceItemDTOSet;

import java.util.Set;

public class InvoiceDTO {
    private String cnpjFornecedor;
    private String employeeUuid;
    private Set<InvoiceItemDTOSet> invoiceItemDTOSetSet;
    private Double totalValue;

    public InvoiceDTO() {
    }

    public InvoiceDTO(String cnpjFornecedor, String employeeUuid, Set<InvoiceItemDTOSet> invoiceItemDTOSetSet, Double totalValue) {
        this.cnpjFornecedor = cnpjFornecedor;
        this.employeeUuid = employeeUuid;
        this.invoiceItemDTOSetSet = invoiceItemDTOSetSet;
        this.totalValue = totalValue;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public Set<InvoiceItemDTOSet> getInvoiceItemDTOSetSet() {
        return invoiceItemDTOSetSet;
    }

    public void setInvoiceItemDTOSetSet(Set<InvoiceItemDTOSet> invoiceItemDTOSetSet) {
        this.invoiceItemDTOSetSet = invoiceItemDTOSetSet;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }
}
