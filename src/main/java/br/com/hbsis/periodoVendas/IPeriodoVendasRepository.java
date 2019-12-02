package br.com.hbsis.periodoVendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {
}
