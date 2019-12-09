package br.com.hbsis.categoriaprodutos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    @Query(value = "select * from categorias where cod_categoria like :code ", nativeQuery = true)
    Optional<CategoriaProduto> findByCode(@Param("code") String code);

}
