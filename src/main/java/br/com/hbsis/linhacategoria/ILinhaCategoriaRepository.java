package br.com.hbsis.linhacategoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {

    @Query(value = "select * from linha_categoria where cod_linha like :code ", nativeQuery = true)
    Optional<LinhaCategoria> findByCode(@Param("code") String code);
}
