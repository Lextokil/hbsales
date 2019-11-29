package br.com.hbsis.produtos;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {



    Optional<Produto> findByCodProduto(String codProduto);



}
