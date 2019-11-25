package br.com.hbsis.produtos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoRest.class);

    private final CategoriaProdutoService categoriaProdutoService;

    @Autowired
    public CategoriaProdutoRest(CategoriaProdutoService categoriaProdutoService) {
        this.categoriaProdutoService = categoriaProdutoService;
    }

    @PostMapping
    public CategoriaProdutoDTO save(@RequestBody CategoriaProdutoDTO categoriaProdutoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", categoriaProdutoDTO);

        return this.categoriaProdutoService.save(categoriaProdutoDTO);
    }

    @GetMapping("/{id}")
    public CategoriaProdutoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.categoriaProdutoService.findById(id);
    }

    @RequestMapping("/all")
    public List<CategoriaProduto> findProdutos() {

        List<CategoriaProduto> categoriaProdutos = categoriaProdutoService.findAll();
        return categoriaProdutos;
    }

    @PutMapping("/{id}")
    public CategoriaProdutoDTO udpate(@PathVariable("id") Long id, @RequestBody CategoriaProdutoDTO categoriaProdutoDTO) {
        LOGGER.info("Recebendo Update para produto de ID: {}", id);
        LOGGER.debug("Payload: {}", categoriaProdutoDTO);

        return this.categoriaProdutoService.update(categoriaProdutoDTO, id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para produto de ID: {}", id);

        this.categoriaProdutoService.delete(id);
    }
}
