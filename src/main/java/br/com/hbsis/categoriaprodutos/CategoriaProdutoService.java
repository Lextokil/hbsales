package br.com.hbsis.categoriaprodutos;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import br.com.hbsis.util.CodeManager;
import br.com.hbsis.util.Extension;
import br.com.hbsis.validation.CnpjValidator;
import com.opencsv.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;
import java.util.List;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;
    private final IFornecedorRepository iFornecedorRepository;

    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService, IFornecedorRepository iFornecedorRepository) {
        this.fornecedorService = fornecedorService;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.iFornecedorRepository = iFornecedorRepository;
    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

        Fornecedor fornecedor = fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedor());

        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando Categoria");
        LOGGER.debug("Categoria: {}", categoriaProdutoDTO);
        LOGGER.debug("Fornecedor da categoria {}", categoriaProdutoDTO.getFornecedor());


        CategoriaProduto categoriaProduto = new CategoriaProduto(
                categoriaProdutoDTO.getCodCategoria(),
                categoriaProdutoDTO.getNome(),
                fornecedor);
        categoriaProduto.setCodCategoria(CodeManager.codCategoriaGenerator(categoriaProduto));


        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);

        return CategoriaProdutoDTO.of(categoriaProduto);
    }

    private void validate(CategoriaProdutoDTO categoriaProdutoDTO) {
        LOGGER.info("Validando categoria");

        if (categoriaProdutoDTO == null) {
            throw new IllegalArgumentException("categoriaDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getCodCategoria())) {
            throw new IllegalArgumentException("Codigo da categoria não deve ser nulo");
        }

        if (!(CodeManager.isCodCategoriaValid(categoriaProdutoDTO.getCodCategoria()))) {
            throw new IllegalArgumentException("Código informado deve conter apenas números e ser menor ou igual a 3 digitos");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nula/vazia");
        }

        if (categoriaProdutoDTO.getFornecedor() == null || categoriaProdutoDTO.getFornecedor() < 1) {
            throw new IllegalArgumentException("ID do fornecedor não deve ser nulo ou menor que 1!!");
        }
    }

    public CategoriaProdutoDTO findById(Long id) {
        Optional<CategoriaProduto> produtoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return CategoriaProdutoDTO.of(produtoOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<CategoriaProduto> findAll() {

        List<CategoriaProduto> categoriaProdutos = iCategoriaProdutoRepository.findAll();

        return categoriaProdutos;
    }

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {
        Optional<CategoriaProduto> produtoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);
        Fornecedor fornecedor = fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedor());


        if (produtoExistenteOptional.isPresent()) {
            CategoriaProduto categoriaProdutoExistente = produtoExistenteOptional.get();
            this.validate(categoriaProdutoDTO);
            LOGGER.info("Atualizando produto... id: [{}]", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Produto Existente: {}", categoriaProdutoExistente);

            validate(categoriaProdutoDTO);

            categoriaProdutoExistente.setCodCategoria(categoriaProdutoDTO.getCodCategoria());
            categoriaProdutoExistente.setNome(categoriaProdutoDTO.getNome());
            categoriaProdutoExistente.setFornecedor(fornecedor);
            categoriaProdutoExistente.setCodCategoria(CodeManager.codCategoriaGenerator(categoriaProdutoExistente));

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return CategoriaProdutoDTO.of(categoriaProdutoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID: [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
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
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();

        List<String[]> linhas = csvReader.readAll();

        for (String[] linha : linhas) {

            String cnpj = CnpjValidator.cleanCnpj(linha[4]);
            Fornecedor fornecedor = iFornecedorRepository.findFirstByCnpj(cnpj);
            Optional<CategoriaProduto> categoria = iCategoriaProdutoRepository.findByCode(linha[1]);
            if (!categoria.isPresent()) {
                try{

                    save(CategoriaProdutoDTO.of(new CategoriaProduto(linha[1], linha[2], fornecedor)));
                }catch (Exception e ){
                    LOGGER.info("Erro ao salvar categoria.");
                    LOGGER.error(e.toString());
                }

            }

        }
    }

    public CategoriaProduto findCategoriaProdutoById(Long id) {
        Optional<CategoriaProduto> categoriaProduto = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProduto.isPresent()) {
            return categoriaProduto.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void exportFromData(HttpServletResponse response) throws IOException, ParseException {
        String filename = "catprod.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        PrintWriter writer1 = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(writer1).
                withSeparator(';').
                withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                withLineEnd(CSVWriter.DEFAULT_LINE_END).
                build();

        String headerCSV[] = {"ID_CATEGORIA", "COD_CATEGORIA", "NOME_CATEGORIA", "RAZÃO_SOCIAL", "CNPJ_FORNECEDOR"};
        icsvWriter.writeNext(headerCSV);
        for (CategoriaProduto linha : this.findAll()) {
            icsvWriter.writeNext(
                    new String[]{linha.getId().toString(), linha.getCodCategoria(),
                    linha.getNome(), linha.getFornecedor().getRazaoSocial(),
                    formatCnpj(linha.getFornecedor().getCnpj())}
                    );
            LOGGER.info("Exportando categoria de produto de ID: {}", linha.getId());
        }
    }

    private String formatCnpj(String cnpj) throws ParseException {
        try {
            MaskFormatter mask = new MaskFormatter("###.###.###/####-##");
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(cnpj);
        } catch (ParseException ex) {
            throw new ParseException("erro so formatar cnpj", 2);
        }
    }
}
