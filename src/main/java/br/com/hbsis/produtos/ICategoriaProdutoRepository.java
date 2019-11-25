package br.com.hbsis.produtos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

}
