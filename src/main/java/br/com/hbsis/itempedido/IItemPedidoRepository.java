package br.com.hbsis.itempedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
