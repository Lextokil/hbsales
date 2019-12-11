package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaprodutos.CategoriaProduto;
import br.com.hbsis.categoriaprodutos.CategoriaProdutoService;
import br.com.hbsis.categoriaprodutos.ICategoriaProdutoRepository;
import br.com.hbsis.util.CodeManager;
import br.com.hbsis.util.Extension;
import com.opencsv.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;


@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;


    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService, ICategoriaProdutoRepository iCategoriaProdutoRepository) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {

        CategoriaProduto categoriaProduto = categoriaProdutoService.findCategoriaProdutoById(linhaCategoriaDTO.getCategoriaProduto());

        this.validate(linhaCategoriaDTO);
        LOGGER.info("Salvando Linha");
        LOGGER.debug("Linha: {}", linhaCategoriaDTO);


        LinhaCategoria linhaCategoria = new LinhaCategoria(
                linhaCategoriaDTO.getCodLinha(),
                linhaCategoriaDTO.getNomeLinha(),
                categoriaProduto
        );
        linhaCategoria.setCodLinha(CodeManager.codLinhaGenerator(linhaCategoriaDTO.getCodLinha()));

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return linhaCategoriaDTO.of(linhaCategoria);
    }

    private void validate(LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Validando Linha ");

        if (linhaCategoriaDTO == null) {
            throw new IllegalArgumentException("Linha de categoria não deve ser nula");
        }

        if (StringUtils.isEmpty(linhaCategoriaDTO.getNomeLinha())) {
            throw new IllegalArgumentException("Nome da linha de categoria não deve ser nulo");
        }

        if (linhaCategoriaDTO.getCodLinha().length() > 10) {
            throw new IllegalArgumentException("Codigo da linha não deve conter mais de 10 caracteres");
        }
        if(StringUtils.isEmpty(linhaCategoriaDTO.getCodLinha())){
            throw new IllegalArgumentException("Codigo da linha não deve ser nulo/vazio");
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

    public LinhaCategoria findLinhaById(Long id) {
        Optional<LinhaCategoria> linhaCategoria = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoria.isPresent()) {
            return linhaCategoria.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public List<LinhaCategoria> findAll() {

        List<LinhaCategoria> linhaCategorias = iLinhaCategoriaRepository.findAll();

        return linhaCategorias;
    }


    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);


        if (linhaCategoriaOptional.isPresent()) {
            LinhaCategoria linhaCategoriaExistente = linhaCategoriaOptional.get();

            validate(linhaCategoriaDTO);

            CategoriaProduto categoriaProduto = categoriaProdutoService.findCategoriaProdutoById(linhaCategoriaDTO.getCategoriaProduto());

            LOGGER.info("Atualizando Linha... id: [{}]", linhaCategoriaExistente.getIdLinhaCategoria());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha Existente: {}", linhaCategoriaExistente);


            linhaCategoriaExistente.setCodLinha(linhaCategoriaDTO.getCodLinha());
            linhaCategoriaExistente.setCategoriaProduto(categoriaProduto);
            linhaCategoriaExistente.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
            linhaCategoriaExistente.setCodLinha(CodeManager.codLinhaGenerator(linhaCategoriaDTO.getCodLinha()));

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

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        PrintWriter writer1 = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer1).
                withSeparator(';').
                withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                withLineEnd(CSVWriter.DEFAULT_LINE_END).
                build();
        String headerCSV[] = {"ID_LINHA_CAT", "COD_LINHA", "NOME_LINHA", "COD_CATEGORIA", "NOME_CATEGORIA"};
        icsvWriter.writeNext(headerCSV);

        for (LinhaCategoria row : this.findAll()) {

            icsvWriter.writeNext(new String[]{String.valueOf(row.getIdLinhaCategoria()), row.getCodLinha(), row.getNomeLinha(),
                    row.getCategoriaProduto().getCodCategoria(), row.getCategoriaProduto().getNome()});
            LOGGER.info("Exportando Linha Categoria ID: {}", row.getIdLinhaCategoria());
        }

    }

    public void saveDataFromUploadFile(MultipartFile file) throws Exception {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (Extension.CSV.getDescricao().equalsIgnoreCase(extension)) {
            readDataFromCsv(file);
        } else {
            throw new Exception("Formato de arquivo invalido");
        }

    }

    private void readDataFromCsv(MultipartFile file) throws IOException {

        InputStreamReader reader = new InputStreamReader(file.getInputStream());
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1).withCSVParser(parser)
                .build();

        List<String[]> linhas = csvReader.readAll();

        for (String[] linha : linhas) {

            try{
                CategoriaProduto categoriaProduto = iCategoriaProdutoRepository.findByCode(linha[3]).get();
                if(!iLinhaCategoriaRepository.findByCode(linha[1]).isPresent()){
                    LinhaCategoria linhaCategoria = new LinhaCategoria(linha[1], linha[2], categoriaProduto);
                    try {
                        save(LinhaCategoriaDTO.of(linhaCategoria));
                    }catch (Exception e){
                        LOGGER.info("Erro ao salvar linha");
                        LOGGER.error(e.toString());
                    }

                }
            }catch (Exception e){
                throw new IllegalArgumentException("Código do fornecedor inválido");
            }




        }
    }
}
