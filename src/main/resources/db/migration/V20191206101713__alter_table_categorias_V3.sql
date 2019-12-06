ALTER TABLE categorias DROP CONSTRAINT AK_fornecedor;

ALTER TABLE categorias
ALTER COLUMN nome VARCHAR(50) NOT NULL;

ALTER TABLE categorias
ALTER COLUMN cod_categoria VARCHAR(10) NOT NULL;

ALTER TABLE categorias
ADD CONSTRAINT AK_fornecedor UNIQUE (id_fornecedor, nome);
