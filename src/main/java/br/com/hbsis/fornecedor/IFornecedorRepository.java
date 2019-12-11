package br.com.hbsis.fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IFornecedorRepository extends JpaRepository<Fornecedor, Long> {

	@Query(value = "select * from fornecedores where cnpj like :cnpj ", nativeQuery = true)
	Fornecedor findFirstByCnpj(@Param("cnpj") String cnpj);

}
