package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoriaProdutos.CategoriaProdutoRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/linhaCategoria")
public class LinhaCategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoRest.class);

    private final LinhaCategoriaService linhaCategoriaService;

    @Autowired
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService) {
        this.linhaCategoriaService = linhaCategoriaService;
    }

    @PostMapping
    public LinhaCategoriaDTO save(@RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.save(linhaCategoriaDTO);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.linhaCategoriaService.findById(id);
    }

    @RequestMapping("/all")
    public List<LinhaCategoria> findAll() {

        List<LinhaCategoria> linhaCategorias = linhaCategoriaService.findAll();
        return linhaCategorias;
    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO udpate(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Recebendo Update para Linha de categoria de ID: {}", id);
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.update(linhaCategoriaDTO, id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Linha de categoria de ID: {}", id);

        this.linhaCategoriaService.delete(id);
    }
}
