create table categorias
(
    id    BIGINT IDENTITY (1, 1) NOT NULL,
    nome VARCHAR(100)           NOT NULL,
    id_fornecedor BIGINT         NOT NULL,
    CONSTRAINT fk_produto_fornecedor FOREIGN KEY (id_fornecedor)
    REFERENCES fornecedores (id)
);