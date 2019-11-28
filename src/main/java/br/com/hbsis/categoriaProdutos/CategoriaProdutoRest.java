package br.com.hbsis.categoriaProdutos;

import com.opencsv.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoRest.class);

    private final CategoriaProdutoService categoriaProdutoService;


    @Autowired
    public CategoriaProdutoRest(CategoriaProdutoService categoriaProdutoService) throws FileNotFoundException {
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



    @PostMapping(value = "/fileupload")
    public String uploadFile(@RequestParam MultipartFile arquivoCsv){
        boolean isFlag = categoriaProdutoService.saveDataFromUploadFile(arquivoCsv);
        if(isFlag){
            LOGGER.info("Successmessage", "File Upload Successfully!");

        }else{
           LOGGER.info("Errormessage", "File Upload not done!");
        }

        return  "redirect:/";
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
    @GetMapping("/export-catprod")
    public void exportCSV(HttpServletResponse response) throws Exception {
        LOGGER.info("Exportando categorias de produtos da base");
        categoriaProdutoService.exportFromData(response);

    }


}
