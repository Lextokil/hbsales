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
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", linhaCategoriaDTO);


        LinhaCategoria linhaCategoria = new LinhaCategoria(
                linhaCategoriaDTO.getCodLinha(),
                linhaCategoriaDTO.getNomeLinha(),
                categoriaProduto
        );
        linhaCategoria.setCodLinha(CodeManager.codLinhaGenerator(linhaCategoriaDTO));

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return linhaCategoriaDTO.of(linhaCategoria);
    }

    private void validate(LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Validando Linha de categoria");

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

            LOGGER.info("Atualizando produto... id: [{}]", linhaCategoriaExistente.getIdLinhaCategoria());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Produto Existente: {}", linhaCategoriaExistente);


            linhaCategoriaExistente.setCodLinha(linhaCategoriaDTO.getCodLinha());
            linhaCategoriaExistente.setCategoriaProduto(categoriaProduto);
            linhaCategoriaExistente.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
            linhaCategoriaExistente.setCodLinha(CodeManager.codLinhaGenerator(linhaCategoriaDTO));

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
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

        List<String[]> linhas = csvReader.readAll();

        for (String[] linha : linhas) {
            String[] linhaTemp = linha[0].replaceAll("\"", "").split(";");

            try{
                CategoriaProduto categoriaProduto = iCategoriaProdutoRepository.findByCode(linhaTemp[3]).get();
                if(!iLinhaCategoriaRepository.findByCode(linhaTemp[1]).isPresent()){
                    LinhaCategoria linhaCategoria = new LinhaCategoria(linhaTemp[1], linhaTemp[2], categoriaProduto);

                    save(LinhaCategoriaDTO.of(linhaCategoria));
                }
            }catch (Exception e){
                throw new IllegalArgumentException("Código do fornecedor inválido");
            }




        }
    }
}
