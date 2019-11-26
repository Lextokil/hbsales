package br.com.hbsis.produtos;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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

        //set file name and content type
        String filename = "catprod.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        PrintWriter writer1 = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(writer1).
                                    withSeparator(';').
                                    withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                                    withLineEnd(CSVWriter.DEFAULT_LINE_END).
                                    build();
        String headerCSV[] = {"ID_PRODUTO", "COD_PRODUTO", "NOME_PRODUTO", "ID_FORNECEDOR"};
        icsvWriter.writeNext(headerCSV);
        for (CategoriaProduto row: categoriaProdutoService.findAll()){
            icsvWriter.writeNext(new String[]{row.getId().toString(),
                    row.getCodCategoria(), row.getNome(), row.getFornecedor().getId().toString()});
        }



    }







}
