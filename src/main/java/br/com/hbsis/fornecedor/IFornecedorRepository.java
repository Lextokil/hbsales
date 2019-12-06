package br.com.hbsis.fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface IFornecedorRepository extends JpaRepository<Fornecedor, Long> {

	@Query(value = "select * from fornecedores where cnpj like '%:cnpj%'", nativeQuery = true)
	List<Fornecedor> findFirstByCnpj(@Param("cnpj") String cnpj);

}
