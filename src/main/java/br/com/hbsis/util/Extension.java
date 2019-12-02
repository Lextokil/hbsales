package br.com.hbsis.util;

public enum Extension {

    /*public static final String CSV = "csv";
    public static final String XML = "xml";
    public static  final String JSON = "json";*/

    CSV("csv"),
    XML("xml"),
    JSON("json");

    private final String descricao;

    Extension(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
