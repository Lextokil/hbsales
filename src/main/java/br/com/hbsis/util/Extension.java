package br.com.hbsis.util;

public enum Extension {


    CSV("csv"),
    XML("xml"),
    JSON("json"),
    ARGS("8============D");

    private final String descricao;

    Extension(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
