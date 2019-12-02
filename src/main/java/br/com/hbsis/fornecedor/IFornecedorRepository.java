package br.com.hbsis.fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public interface IFornecedorRepository extends JpaRepository<Fornecedor, Long> {

    default List<Fornecedor> findFornecedor(String cnpj){
        return Collections.emptyList();
    }
}
