package br.com.hbsis.util;

public enum UnidadeMedida {

    MG("miligramas"),
    G("gramas"),
    KG("quilogramas");

    private final String descricao;

    UnidadeMedida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
