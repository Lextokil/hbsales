ALTER TABLE categorias
ADD CONSTRAINT AK_fornecedor UNIQUE (id_fornecedor, nome);