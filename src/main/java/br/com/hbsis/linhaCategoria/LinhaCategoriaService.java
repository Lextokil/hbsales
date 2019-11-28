package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoriaProdutos.CategoriaProdutoService;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;


@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;


    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {

        linhaCategoriaDTO.setCategoriaProduto(categoriaProdutoService.findCategoriaProdutoById(linhaCategoriaDTO.getCategoriaProduto().getId()));


        this.validate(linhaCategoriaDTO);
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", linhaCategoriaDTO);



        LinhaCategoria linhaCategoria = new LinhaCategoria(
                                linhaCategoriaDTO.getCodLinha(),
                                linhaCategoriaDTO.getNomeLinha(),
                                linhaCategoriaDTO.getCategoriaProduto()
        );



        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return linhaCategoriaDTO.of(linhaCategoria);
    }

    private void validate(LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Validando Linha de categoria");

        if (linhaCategoriaDTO == null) {
            throw new IllegalArgumentException("Linha de categoria não deve ser nula");
        }

        if (StringUtils.isEmpty(linhaCategoriaDTO.getNomeLinha())){
            throw new IllegalArgumentException("Nome da linha de categoria não deve ser nulo");
        }

        if (StringUtils.isEmpty(linhaCategoriaDTO.getCodLinha())) {
            throw new IllegalArgumentException("Codigo da linha não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(linhaCategoriaDTO.getCategoriaProduto().toString())) {
            throw new IllegalArgumentException("Categoria de produto da linha não deve ser nulo/vazio");
        }
    }

    public LinhaCategoriaDTO findById(Long id) {
        Optional<LinhaCategoria> linhaCategoria = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoria.isPresent()) {
            return LinhaCategoriaDTO.of(linhaCategoria.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public List<LinhaCategoria> findAll() {

        List<LinhaCategoria> linhaCategorias =  iLinhaCategoriaRepository.findAll();

        return linhaCategorias;
    }



    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);
        linhaCategoriaDTO.setCategoriaProduto(categoriaProdutoService.findCategoriaProdutoById
                (linhaCategoriaDTO.getCategoriaProduto().getId()));


        if (linhaCategoriaOptional.isPresent()) {
            LinhaCategoria linhaCategoriaExistente = linhaCategoriaOptional.get();


            LOGGER.info("Atualizando produto... id: [{}]", linhaCategoriaExistente.getIdLinhaCategoria());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Produto Existente: {}", linhaCategoriaExistente);

            linhaCategoriaExistente.setCodLinha(linhaCategoriaDTO.getCodLinha());
            linhaCategoriaExistente.setCategoriaProduto(linhaCategoriaDTO.getCategoriaProduto());
            linhaCategoriaExistente.setNomeLinha(linhaCategoriaDTO.getNomeLinha());

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return linhaCategoriaDTO.of(linhaCategoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para Linha de categoria de ID: [{}]", id);

        this.iLinhaCategoriaRepository.deleteById(id);
    }

    public void exportFromData(HttpServletResponse response) throws IOException {
        String filename = "linhacat.csv";
        Boolean succes = false;

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        PrintWriter writer1 = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer1).
                withSeparator(';').
                withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                withLineEnd(CSVWriter.DEFAULT_LINE_END).
                build();
        String headerCSV[] = {"ID_LINHA_CAT", "COD_LINHA", "NOME_LINHA", "ID_CATEGORIA"};
        icsvWriter.writeNext(headerCSV);

        for (LinhaCategoria row: this.findAll()){
            icsvWriter.writeNext(new String[]{String.valueOf(row.getIdLinhaCategoria()), row.getCodLinha(), row.getNomeLinha(),
                    String.valueOf(row.getCategoriaProduto().getId())});
            LOGGER.info("Exportando Linha Categoria ID: {}", row.getIdLinhaCategoria());
        }

    }
}
