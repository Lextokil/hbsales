CREATE TABLE produtos(
    id   BIGINT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
    cod_produto VARCHAR(100)           NOT NULL,
    nome_produto VARCHAR(100)          NOT NULL,
    preco_produto FLOAT(53)            NOT NULL,
    unidade_caixa INT                  NOT NULL,
    peso_unidade  FLOAT(24)            NOT NULL,
    validade_produto  VARCHAR(100)     NOT NULL,
    id_linha BIGINT                    NOT NULL,
    CONSTRAINT fk_produto_linha FOREIGN KEY (id_linha)
    REFERENCES linha_categoria (id)
);