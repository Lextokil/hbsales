ALTER TABLE categorias
ADD CONSTRAINT UK_fornecedor_Code UNIQUE (id_fornecedor, cod_categoria);