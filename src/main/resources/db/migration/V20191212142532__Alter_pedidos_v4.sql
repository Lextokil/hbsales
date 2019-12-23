ALTER TABLE pedidos
ADD status VARCHAR(15);

ALTER TABLE pedidos
ADD codigo VARCHAR(50)UNIQUE;

ALTER TABLE pedidos
ADD data_criacao DATE;