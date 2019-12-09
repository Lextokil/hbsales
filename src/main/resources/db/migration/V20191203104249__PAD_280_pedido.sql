CREATE TABLE pedidos(
      id    BIGINT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
      valor_total FLOAT(53)             NOT NULL,

);

CREATE TABLE item_pedido(
      id_pedido  BIGINT             NOT NULL,
      id_produto BIGINT             NOT NULL,
      quantidade INT                NOT NULL,
      CONSTRAINT fk_item_pedido_produto FOREIGN KEY (id_produto)
      REFERENCES produtos (id),
      CONSTRAINT fk_item_pedido         FOREIGN KEY (id_pedido)
      REFERENCES pedidos (id)
);