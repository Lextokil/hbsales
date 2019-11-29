package br.com.hbsis.produtos;

import br.com.hbsis.linhaCategoria.LinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;

    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {

        LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(produtoDTO.getLinhaCategoria());

        this.validate(produtoDTO);
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", produtoDTO);



        Produto produto = new Produto(
                produtoDTO.getCodProduto(),
                produtoDTO.getNomeProduto(),
                produtoDTO.getPrecoProduto(),
                produtoDTO.getUnidadeProduto(),
                produtoDTO.getPesoUnidade(),
                produtoDTO.getValidadeProduto(),
                linhaCategoria
        );



        produto = this.iProdutoRepository.save(produto);

        return produtoDTO.of(produto);
    }

    private void validate(ProdutoDTO produtoDTO) {
        LOGGER.info("Validando Linha de categoria");

        if (produtoDTO == null) {
            throw new IllegalArgumentException("Produto não deve ser nulo");
        }

        if (StringUtils.isEmpty(produtoDTO.getCodProduto())){
            throw new IllegalArgumentException("Codigo do produto não deve ser nulo");
        }

        if (StringUtils.isEmpty(produtoDTO.getNomeProduto())) {
            throw new IllegalArgumentException("Nome do produto não deve ser nulo/vazio");
        }

        if (StringUtils.isEmpty(produtoDTO.getPrecoProduto().toString())) {
            throw new IllegalArgumentException("Produto deve ter um preço");
        }
        if (StringUtils.isEmpty(produtoDTO.getPesoUnidade().toString())) {
            throw new IllegalArgumentException("Produto deve ter um peso por unidade");
        }
        if (StringUtils.isEmpty(produtoDTO.getValidadeProduto())) {
            throw new IllegalArgumentException("Produto tem que ter uma validade");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getUnidadeProduto()))) {
            throw new IllegalArgumentException("Produto tem que ter uma validade");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getLinhaCategoria()))) {
            throw new IllegalArgumentException("Linha do produto tem que ter um ID");
        }
    }
    public ProdutoDTO findById(Long id) {
        Optional<Produto> produto = this.iProdutoRepository.findById(id);

        if (produto.isPresent()) {
            return ProdutoDTO.of(produto.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Produto> findAll() {

        List<Produto> produtos =  iProdutoRepository.findAll();

        return produtos;
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);



        if (produtoOptional.isPresent()) {
            Produto produtoExistente = produtoOptional.get();
            LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(produtoDTO.getLinhaCategoria());


            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto Existente: {}", produtoExistente);

            produtoExistente.setCodProduto(produtoDTO.getCodProduto());
            produtoExistente.setNomeProduto(produtoDTO.getNomeProduto());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setPrecoProduto(produtoDTO.getPrecoProduto());
            produtoExistente.setUnidadeProduto(produtoDTO.getUnidadeProduto());
            produtoExistente.setValidadeProduto(produtoDTO.getValidadeProduto());
            produtoExistente.setLinhaCategoria(linhaCategoria);

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return produtoDTO.of(produtoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID: [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }
}
