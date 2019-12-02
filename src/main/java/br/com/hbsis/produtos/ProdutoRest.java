package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);

    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;

    @Autowired
    public ProdutoRest(ProdutoService produtoService, FornecedorService fornecedorService) {
        this.produtoService = produtoService;
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public ProdutoDTO save(@RequestBody ProdutoDTO produtoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", produtoDTO);

        return this.produtoService.save(produtoDTO);
    }

    @GetMapping("/{id}")
    public ProdutoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo produto de ID: [{}]", id);
        return this.produtoService.findById(id);
    }

    @GetMapping("/export-produtos")
    public void exportCSV(HttpServletResponse response) throws Exception {
        LOGGER.info("Exportando linha de categorias {}");
        produtoService.exportFromData(response);

    }

    @PostMapping(value = "/fileupload")
    public void uploadFile(@RequestParam MultipartFile arquivoCsv) throws Exception {
        try {
            produtoService.saveDataFromUploadFile(arquivoCsv);
        } catch (Exception e) {
            String erroAoImportarCSV = "Erro ao importar CSV";
            LOGGER.error(erroAoImportarCSV, e);
            throw new Exception(erroAoImportarCSV);
        }

        LOGGER.info("Successmessage", "File Upload Successfully!");
    }

    @PostMapping(value = "/fileupload/{id}")
    public void uploadFileFromFornecedor(@RequestParam MultipartFile arquivoCsv, @PathVariable("id") Long id) throws Exception {
        try {
            FornecedorDTO fornecedorDTOExistente = fornecedorService.findById(id);
            produtoService.saveProdutosWithFornecedorID(arquivoCsv, fornecedorDTOExistente.getId());
        } catch (Exception e) {
            String erroAoImpotarCSV = "Erro ao importar CSV";
            LOGGER.error(erroAoImpotarCSV, e);
            throw new Exception(erroAoImpotarCSV);
        }


        LOGGER.info("Successmessage", "File Upload Successfully!");

    }

    @RequestMapping("/all")
    public List<Produto> findAll() {
        List<Produto> produtos = produtoService.findAll();
        return produtos;
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO) {
        LOGGER.info("Recebendo Update para produto de ID: {}", id);
        LOGGER.debug("Payload: {}", produtoDTO);

        return this.produtoService.update(produtoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.produtoService.delete(id);
    }


}
