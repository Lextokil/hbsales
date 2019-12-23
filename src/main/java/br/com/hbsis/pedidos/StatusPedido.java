package br.com.hbsis.pedidos;

public enum StatusPedido {
    ATIVO("ATIVO"),
    CANCELADO("CANCELADO"),
    RETIRADO("RETIRADO");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
