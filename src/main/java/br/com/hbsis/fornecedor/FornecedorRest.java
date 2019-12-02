package br.com.hbsis.fornecedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorRest.class);

    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorRest(FornecedorService fornecedorService){
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public FornecedorDTO save(@RequestBody FornecedorDTO fornecedorDTO){
        LOGGER.info("Recebendo solicitação de persistência de Fornecedor...");
        LOGGER.debug("Payaload: {}", fornecedorDTO);
       return this.fornecedorService.save(fornecedorDTO);
    }

    @GetMapping("/{id}")
    public FornecedorDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.fornecedorService.findById(id);
    }
    @RequestMapping("/all")
    public List<Fornecedor> findFornecedores() {

        List<Fornecedor> fornecedores = fornecedorService.findAll();
        return fornecedores;
    }

    @PutMapping("/{id}")
    public FornecedorDTO udpate(@PathVariable("id") Long id, @RequestBody FornecedorDTO fornecedorDTO) {
        LOGGER.info("Recebendo Update para fornecedor de ID: {}", id);
        LOGGER.debug("Payload: {}", fornecedorDTO);

        return this.fornecedorService.update(fornecedorDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para fornecedor de ID: {}", id);

        this.fornecedorService.delete(id);
    }
}
