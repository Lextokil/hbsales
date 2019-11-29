package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    public ProdutoDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebendo produto de ID: [{}]", id);
        return this.produtoService.findById(id);
    }

    @GetMapping("/export-produtos")
    public void exportCSV(HttpServletResponse response) throws Exception {
        LOGGER.info("Exportando linha de categorias {}");
        produtoService.exportFromData(response);

    }

    @PostMapping(value = "/fileupload")
    public String uploadFile(@RequestParam MultipartFile arquivoCsv){
        boolean isFlag = produtoService.saveDataFromUploadFile(arquivoCsv);
        if(isFlag){
            LOGGER.info("Successmessage", "File Upload Successfully!");

        }else{
            LOGGER.info("Errormessage", "File Upload not done!");
        }

        return  "redirect:/";
    }
    @PostMapping(value = "/fileupload/{id}")
    public String uploadFileFromFornecedor(@RequestParam MultipartFile arquivoCsv, @PathVariable("id")Long id){
        FornecedorDTO fornecedorDTOExistente =  fornecedorService.findById(id);
        boolean isFlag = produtoService.saveProdutosWithFornecedorID(arquivoCsv, fornecedorDTOExistente.getId());
        if(isFlag){
            LOGGER.info("Successmessage", "File Upload Successfully!");

        }else{
            LOGGER.info("Errormessage", "File Upload not done!");
        }

        return  "redirect:/";
    }

    @RequestMapping("/all")
    public List<Produto> findAll(){
        List<Produto> produtos = produtoService.findAll();
        return produtos;
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO){
        LOGGER.info("Recebendo Update para produto de ID: {}", id);
        LOGGER.debug("Payload: {}", produtoDTO);

        return  this.produtoService.update(produtoDTO, id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        this.produtoService.delete(id);
    }




}
