package br.com.hbsis.produtos;

import br.com.hbsis.categoriaprodutos.CategoriaProduto;
import br.com.hbsis.categoriaprodutos.CategoriaProdutoDTO;
import br.com.hbsis.categoriaprodutos.CategoriaProdutoService;
import br.com.hbsis.categoriaprodutos.ICategoriaProdutoRepository;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import br.com.hbsis.linhacategoria.ILinhaCategoriaRepository;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import br.com.hbsis.util.CodeManager;
import br.com.hbsis.util.DateValidator;
import br.com.hbsis.util.Extension;
import br.com.hbsis.util.UnidadeMedida;
import br.com.hbsis.validation.CnpjValidator;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.opencsv.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final IFornecedorRepository iFornecedorRepository;
    private final CategoriaProdutoService categoriaProdutoService;

    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService, ILinhaCategoriaRepository iLinhaCategoriaRepository, ICategoriaProdutoRepository iCategoriaProdutoRepository, IFornecedorRepository iFornecedorRepository, CategoriaProdutoService categoriaProdutoService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.iFornecedorRepository = iFornecedorRepository;
        this.categoriaProdutoService = categoriaProdutoService;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {


        LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(produtoDTO.getLinhaCategoria());

        this.validate(produtoDTO);
         LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", produtoDTO);

        produtoDTO.setCodProduto(CodeManager.generateProdutoCode(produtoDTO.getCodProduto()));


        Produto produto = new Produto(
                produtoDTO.getCodProduto(),
                produtoDTO.getNomeProduto(),
                produtoDTO.getPrecoProduto(),
                produtoDTO.getUnidadeProduto(),
                produtoDTO.getPesoUnidade(),
                produtoDTO.getUnidadeMedida(),
                DateValidator.convertToLocalDateTime(produtoDTO.getValidadeProduto()),
                linhaCategoria
        );


        produto = this.iProdutoRepository.save(produto);

        return produtoDTO.of(produto);
    }

    private void validate(ProdutoDTO produtoDTO) {
        LOGGER.info("Validando Produto");

        if (produtoDTO == null) {
            throw new IllegalArgumentException("Produto não deve ser nulo");
        }

        if (StringUtils.isEmpty(produtoDTO.getCodProduto())) {
            throw new IllegalArgumentException("Codigo do produto não deve ser nulo");
        }
        if (produtoDTO.getCodProduto().length() > 10) {
            throw new IllegalArgumentException("Codigo não deve ter mais de 10 caracteres");
        }

        if (StringUtils.isEmpty(produtoDTO.getNomeProduto())) {
            throw new IllegalArgumentException("Nome do produto não deve ser nulo/vazio");
        }

        if (StringUtils.isEmpty(produtoDTO.getPrecoProduto().toString())) {
            throw new IllegalArgumentException("Produto deve ter um preço");
        }
        if (StringUtils.isEmpty(produtoDTO.getPesoUnidade().toString())) {
            throw new IllegalArgumentException("Produto deve ter um peso por unidade");
        }
        if (!EnumUtils.isValidEnum(UnidadeMedida.class, produtoDTO.getUnidadeMedida())) {
            throw new IllegalArgumentException("Unidade de medida inválida");
        }
        if (produtoDTO.getValidadeProduto().equals(null)) {
            throw new IllegalArgumentException("Produto tem que ter uma validade");
        }

        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getUnidadeProduto()))) {
            throw new IllegalArgumentException("Produto tem que ter uma validade");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getLinhaCategoria()))) {
            throw new IllegalArgumentException("Linha do produto tem que ter um ID");
        }
    }

    public ProdutoDTO findById(Long id) {

        Optional<Produto> produto = this.iProdutoRepository.findById(id);

        if (produto.isPresent()) {
            return ProdutoDTO.of(produto.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Produto findProdutoById(Long id) {

        Optional<Produto> produto = this.iProdutoRepository.findById(id);

        if (produto.isPresent()) {
            return produto.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Produto> findAll() {

        List<Produto> produtos = iProdutoRepository.findAll();

        return produtos;
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);


        if (produtoOptional.isPresent()) {
            Produto produtoExistente = produtoOptional.get();
            LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(produtoDTO.getLinhaCategoria());
            validate(produtoDTO);
            produtoDTO.setCodProduto(CodeManager.generateProdutoCode(produtoDTO.getCodProduto()));


            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto Existente: {}", produtoExistente);

            produtoExistente.setCodProduto(produtoDTO.getCodProduto());
            produtoExistente.setNomeProduto(produtoDTO.getNomeProduto());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setPrecoProduto(produtoDTO.getPrecoProduto());
            produtoExistente.setUnidadeProduto(produtoDTO.getUnidadeProduto());
            produtoExistente.setUnidadeMedida(produtoDTO.getUnidadeMedida());
            produtoExistente.setValidadeProduto(DateValidator.convertToLocalDateTime(produtoDTO.getValidadeProduto()));
            produtoExistente.setLinhaCategoria(linhaCategoria);

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return produtoDTO.of(produtoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID: [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }

    public void exportFromData(HttpServletResponse response) throws IOException, ParseException {
        String filename = "produtos.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        PrintWriter writer1 = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer1).
                withSeparator(';').
                withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                withLineEnd(CSVWriter.DEFAULT_LINE_END).
                build();
        String headerCSV[] = {"ID", "COD_PRODUTO", "NOME_PRODUTO", "PRECO_PRODUTO", "UNIDADE_CAIXA", "PESO_UNIDADE",
                "VALIDADE", "COD_LINHA", "NOME_LINHA", "COD_CATEGORIA", "NOME_CATEGORIA", "CNPJ_FORNECEDOR", "RAZAO_SOCIAL"};
        icsvWriter.writeNext(headerCSV);

        for (Produto produtoRow : this.findAll()) {

            LinhaCategoria lc = produtoRow.getLinhaCategoria();
            CategoriaProduto cp = lc.getCategoriaProduto();
            Fornecedor f = cp.getFornecedor();

            icsvWriter.writeNext(new String[]{
                    String.valueOf(produtoRow.getId()), produtoRow.getCodProduto(), produtoRow.getNomeProduto(),
                    ("R$:" + formatDecimal(produtoRow.getPrecoProduto())), String.valueOf(produtoRow.getUnidadeProduto()),
                    (formatDecimal(produtoRow.getPesoUnidade()) + produtoRow.getUnidadeMedida()),
                    DateValidator.convertDateToString(produtoRow.getValidadeProduto()),
                    String.valueOf(lc.getCodLinha()), lc.getNomeLinha(), cp.getCodCategoria(),
                    cp.getNome(), CnpjValidator.formatCnpj(f.getCnpj()), f.getRazaoSocial()});

            LOGGER.info("Exportando Produto de ID: {}", produtoRow.getId());
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

    public void saveProdutosWithFornecedorID(MultipartFile file, Long id) throws Exception {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (Extension.CSV.getDescricao().equalsIgnoreCase(extension)) {
            readDataFromCsvWithFornecedorID(file, id);
        } else {
            throw new Exception("Formato de arquivo invalido");
        }

    }

    private void readDataFromCsv(MultipartFile file) throws IOException {

        InputStreamReader reader = new InputStreamReader(file.getInputStream());
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<String[]> linhas = csvReader.readAll();

        for (String[] linha : linhas) {

            LinhaCategoria linhaCategoria = iLinhaCategoriaRepository.findByCode(linha[7]).get();
            Double preco = Double.parseDouble(linha[3].replaceAll("[^0-9.,]", ""));
            Double peso = Double.parseDouble(linha[5].replaceAll("[^0-9.,]", ""));
            String unidadeMedida = linha[5].replaceAll("[0-9.,]", "");
            LocalDateTime validade = DateValidator.convertToLocalDateTime(linha[6]);

            Produto produto = new Produto(linha[1], linha[2], preco, Integer.parseInt(linha[4]),
                    peso, unidadeMedida, validade, linhaCategoria);
            produto.setCodProduto(CodeManager.generateProdutoCode(produto.getCodProduto()));
            validate(ProdutoDTO.of(produto));

            this.iProdutoRepository.save(produto);
        }

    }

    private void readDataFromCsvWithFornecedorID(MultipartFile file, Long idFornecedor) throws IOException {


        InputStreamReader reader = new InputStreamReader(file.getInputStream());
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<String[]> linhas = csvReader.readAll();
        Fornecedor fornecedorDoProduto = iFornecedorRepository.findById(idFornecedor).get();

        for (String[] linha : linhas) {

            try {
                CategoriaProdutoDTO categoriaProdutoDTO = getOrCreateCategoria(linha[9], linha[10], fornecedorDoProduto);

                LinhaCategoriaDTO linhaCategoria = getOrCreateLinha(linha[8], linha[7], categoriaProdutoDTO);

                ProdutoDTO produtoDTO = new ProdutoDTO();
                produtoDTO.setCodProduto(linha[1]);
                produtoDTO.setNomeProduto(linha[2]);
                Double preco = Double.parseDouble(linha[3].replaceAll("[^0-9.,]", ""));
                produtoDTO.setPrecoProduto(preco);
                produtoDTO.setUnidadeProduto(Integer.parseInt(linha[4]));
                Double peso = Double.parseDouble(linha[5].replaceAll("[^0-9.,]", ""));
                produtoDTO.setPesoUnidade(peso);
                String unidadeMedida = linha[5].replaceAll("[0-9.,]", "");
                produtoDTO.setUnidadeMedida(unidadeMedida);
                produtoDTO.setValidadeProduto(linha[6]);
                produtoDTO.setLinhaCategoria(linhaCategoria.getIdLinhaCategoria());

                if (categoriaProdutoDTO.getFornecedor() == fornecedorDoProduto.getId()) {

                    createOrUpdateProduto(produtoDTO, linhaCategoria);

                } else {
                    LOGGER.info("Produto de outro fornecedor");
                }
            } catch (Exception e) {
                LOGGER.info("Linha do CSV Inválida");
            }
        }
    }


    public String formatDecimal(Double valor) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorFormatado = df.format(valor);
        return valorFormatado;
    }

    public CategoriaProdutoDTO getOrCreateCategoria(String codCategoria, String nomeCategoria, Fornecedor fornecedor) {
        String codCategoriatemp = codCategoria;
        if (codCategoria.length() <= 3) {
            codCategoriatemp = CodeManager.codCategoriaGenerator(new CategoriaProduto(codCategoria, nomeCategoria, fornecedor));
        }
        CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();

        Optional<CategoriaProduto> categoriaExistente = iCategoriaProdutoRepository.findByCode(codCategoriatemp);

        if (categoriaExistente.isPresent()) {
            categoriaProdutoDTO = CategoriaProdutoDTO.of(categoriaExistente.get());
            LOGGER.info("Retornando Categoria existente de id: {};", categoriaProdutoDTO.getId());
            return categoriaProdutoDTO;
        } else {
            categoriaProdutoDTO.setNome(nomeCategoria);
            categoriaProdutoDTO.setCodCategoria(codCategoria);
            categoriaProdutoDTO.setFornecedor(fornecedor.getId());
            try {
                categoriaProdutoDTO = categoriaProdutoService.save(categoriaProdutoDTO);
            } catch (Exception e) {
                LOGGER.info("Erro ao salvar Categoria");
                LOGGER.error(e.toString());
            }

            return categoriaProdutoDTO;
        }
    }

    public LinhaCategoriaDTO getOrCreateLinha(String nomeLinha, String codLinha, CategoriaProdutoDTO categoriaProdutoDTO) {
        codLinha = CodeManager.codLinhaGenerator(codLinha);
        Optional<LinhaCategoria> linhaExistente = iLinhaCategoriaRepository.findByCode(codLinha);

        LinhaCategoriaDTO linhaCategoriaDTO = new LinhaCategoriaDTO();

        if (linhaExistente.isPresent()) {
            linhaCategoriaDTO = LinhaCategoriaDTO.of(linhaExistente.get());

            if (linhaCategoriaDTO.getCategoriaProduto() != categoriaProdutoDTO.getId()) {
                linhaCategoriaDTO.setCategoriaProduto(categoriaProdutoDTO.getId());
                try {
                    linhaCategoriaDTO = linhaCategoriaService.update(linhaCategoriaDTO, linhaCategoriaDTO.getIdLinhaCategoria());
                } catch (Exception e) {
                    LOGGER.info("Erro ao dar um update na Linha");
                    LOGGER.error(e.toString());
                }

            }
            LOGGER.info("Retornando linha updatada: {}", linhaCategoriaDTO.getIdLinhaCategoria());
            return linhaCategoriaDTO;
        } else {
            linhaCategoriaDTO.setNomeLinha(nomeLinha);
            linhaCategoriaDTO.setCodLinha(CodeManager.codLinhaGenerator(codLinha));
            linhaCategoriaDTO.setCategoriaProduto(categoriaProdutoDTO.getId());

            try {
                linhaCategoriaDTO = linhaCategoriaService.save(linhaCategoriaDTO);
            } catch (Exception e) {
                LOGGER.info("Erro ao salvar Linha");
                LOGGER.error(e.toString());
            }
            LOGGER.info("Retornando nova linha: {}", linhaCategoriaDTO.getIdLinhaCategoria());
            return linhaCategoriaDTO;
        }
    }

    public void createOrUpdateProduto(ProdutoDTO produtoDTO, LinhaCategoriaDTO linhaCategoria) {
        String codeToFind = produtoDTO.getCodProduto();
        if (codeToFind.length() < 10) {
            codeToFind = CodeManager.generateProdutoCode(produtoDTO.getCodProduto());
        }
        Optional<Produto> produtoexistente = iProdutoRepository.findByCodProduto(codeToFind);
        if (produtoexistente.isPresent()) {
            produtoDTO = ProdutoDTO.of(produtoexistente.get());
            produtoDTO.setLinhaCategoria(linhaCategoria.getIdLinhaCategoria());
            try {
                update(produtoDTO, produtoDTO.getIdProduto());
            } catch (Exception e) {
                LOGGER.info("Erro ao dar update no Produto");
                LOGGER.error(e.toString());
            }


        } else {
            try {
                save(produtoDTO);
            } catch (Exception e) {
                LOGGER.info("Erro ao salvar Produto");
                LOGGER.error(e.toString());
            }

        }

    }
}
