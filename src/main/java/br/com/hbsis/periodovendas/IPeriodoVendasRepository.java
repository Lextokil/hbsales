package br.com.hbsis.periodovendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {

    @Query(value = "select * from periodo_vendas where id_fornecedor like :idFornecedor ", nativeQuery = true)
    List<PeriodoVendas> findPeriodoByFornecedor(@Param("idFornecedor") Long idFornecedor);
}
