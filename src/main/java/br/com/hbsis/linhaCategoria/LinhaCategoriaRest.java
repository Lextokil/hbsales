package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoriaProdutos.CategoriaProdutoRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    @GetMapping("/export-linhacat")
    public void exportCSV(HttpServletResponse response) throws Exception {
        LOGGER.info("Exportando linha de categorias {}");
        linhaCategoriaService.exportFromData(response);

    }

    @PostMapping(value = "/fileupload")
    public void uploadFile(@RequestParam MultipartFile arquivoCsv) throws Exception {
        try {
            linhaCategoriaService.saveDataFromUploadFile(arquivoCsv);
        }catch (Exception e)  {
            String erroAoImportarCsv = "Erro ao importar CSV";
            LOGGER.error(erroAoImportarCsv, e);
            throw new Exception(erroAoImportarCsv);
        }
        LOGGER.info("Successmessage File Upload Successfully!");

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
