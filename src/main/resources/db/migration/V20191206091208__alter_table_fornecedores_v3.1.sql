
ALTER TABLE fornecedores
ALTER COLUMN nome VARCHAR(100) NOT NULL;

ALTER TABLE fornecedores
ALTER COLUMN endereco VARCHAR(100) NOT NULL;

ALTER TABLE fornecedores
ALTER COLUMN telefone VARCHAR(12) NOT NULL;

ALTER TABLE fornecedores
ALTER COLUMN email VARCHAR(50) NOT NULL;

ALTER TABLE fornecedores
ALTER COLUMN cnpj VARCHAR(14) NOT NULL;

ALTER TABLE fornecedores
add constraint UK_cnpj unique (cnpj);
