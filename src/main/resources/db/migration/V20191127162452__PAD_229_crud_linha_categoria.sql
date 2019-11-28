ALTER TABLE produtos
ADD PRIMARY KEY (id);

CREATE TABLE linha_categoria(
    id_linha_categoria    BIGINT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
    cod_linha VARCHAR(100)           NOT NULL,
    nome_linha VARCHAR(100)          NOT NULL,
    id_categoria BIGINT         NOT NULL,
    CONSTRAINT fk_linha_produtos FOREIGN KEY (id_categoria)
    REFERENCES produtos (id)
);