package br.com.hbsis.produtos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    private  final IProdutoRepository iProdutoRepository;

    @Autowired
    public ProdutoService(IProdutoRepository iProdutoRepository){
        this.iProdutoRepository = iProdutoRepository;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO){
        Produto produto = new Produto(produtoDTO.getNome(),
                    produtoDTO.getFornecedor());
        produto = this.iProdutoRepository.save(produto);
        return produtoDTO.of(produto);

    }
}
