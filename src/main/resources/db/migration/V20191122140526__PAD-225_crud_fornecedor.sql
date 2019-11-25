create table fornecedores
(
    id  BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
    razao_social VARCHAR (100) NOT NULL,
    cnpj VARCHAR (50)  NOT NULL,
    nome VARCHAR (255) NOT NULL,
    endereco VARCHAR (255) NOT NULL,
    telefone VARCHAR (36) NOT NULL,
    email VARCHAR (255) NOT NULL,


);