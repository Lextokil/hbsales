CREATE TABLE periodo_vendas(
    id   BIGINT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
    data_inicio   DATE          NOT NULL,
    data_final    DATE          NOT NULL,
    data_retirada DATE          NOT NULL,
    id_fornecedor BIGINT        NOT NULL,
    CONSTRAINT fk_pv_fornecedor FOREIGN KEY(id_fornecedor)
    REFERENCES fornecedores(id)
);