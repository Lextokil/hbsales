package br.com.hbsis.produtos;

import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
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
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;

    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {


        LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(produtoDTO.getLinhaCategoria());

        this.validate(produtoDTO);
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", produtoDTO);


        Produto produto = new Produto(
                produtoDTO.getCodProduto(),
                produtoDTO.getNomeProduto(),
                produtoDTO.getPrecoProduto(),
                produtoDTO.getUnidadeProduto(),
                produtoDTO.getPesoUnidade(),
                produtoDTO.getValidadeProduto(),
                linhaCategoria
        );


        produto = this.iProdutoRepository.save(produto);

        return produtoDTO.of(produto);
    }

    private void validate(ProdutoDTO produtoDTO) {
        LOGGER.info("Validando Linha de categoria");

        if (produtoDTO == null) {
            throw new IllegalArgumentException("Produto não deve ser nulo");
        }

        if (StringUtils.isEmpty(produtoDTO.getCodProduto())) {
            throw new IllegalArgumentException("Codigo do produto não deve ser nulo");
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
        if (StringUtils.isEmpty(produtoDTO.getValidadeProduto())) {
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


            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto Existente: {}", produtoExistente);

            produtoExistente.setCodProduto(produtoDTO.getCodProduto());
            produtoExistente.setNomeProduto(produtoDTO.getNomeProduto());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setPrecoProduto(produtoDTO.getPrecoProduto());
            produtoExistente.setUnidadeProduto(produtoDTO.getUnidadeProduto());
            produtoExistente.setValidadeProduto(produtoDTO.getValidadeProduto());
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

    public void exportFromData(HttpServletResponse response) throws IOException {
        String filename = "produtos.csv";
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
        String headerCSV[] = {"ID", "COD_PRODUTO", "NOME_PRODUTO", "PRECO_PRODUTO", "UNIDADE_PRODUTO", "PESO_UNIDADE", "VALIDADE", "ID_LINHA"};
        icsvWriter.writeNext(headerCSV);

        for (Produto row : this.findAll()) {
            icsvWriter.writeNext(new String[]{String.valueOf(row.getId()), row.getCodProduto(), row.getNomeProduto(),
                    String.valueOf(row.getPrecoProduto()), String.valueOf(row.getUnidadeProduto()),
                    String.valueOf(row.getPesoUnidade()), row.getValidadeProduto(), String.valueOf(row.getLinhaCategoria().getIdLinhaCategoria())});
            LOGGER.info("Exportando Linha Categoria ID: {}", row.getId());
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
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

        List<String[]> linhas = csvReader.readAll();

        for (String[] linha : linhas) {
            String[] linhaTemp = linha[0].replaceAll("\"", "").split(";");

            LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(Long.parseLong(linhaTemp[7]));

            Produto produto = new Produto(linhaTemp[1], linhaTemp[2], Double.parseDouble(linhaTemp[3]), Integer.parseInt(linhaTemp[4]),
                    Double.parseDouble(linhaTemp[5]), linhaTemp[6], linhaCategoria);

            this.iProdutoRepository.save(produto);
        }

    }

    private void readDataFromCsvWithFornecedorID(MultipartFile file, Long idFornecedor) throws IOException {


            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

            List<String[]> linhas = csvReader.readAll();

            for (String[] linha : linhas) {
                String[] linhaTemp = linha[0].replaceAll("\"", "").split(";");

                boolean verificarFornecedor = this.checkFornecedorFromProduto(Long.parseLong(linhaTemp[7]), idFornecedor);

                if (verificarFornecedor) {

                    Optional<Produto> produtoExistente = iProdutoRepository.findByCodProduto(linhaTemp[1]);

                    if (produtoExistente.isPresent()) {

                        ProdutoDTO produtoDTOUpdate = new ProdutoDTO();
                        produtoDTOUpdate.setIdProduto(produtoExistente.get().getId());
                        produtoDTOUpdate.setCodProduto(linhaTemp[1]);
                        produtoDTOUpdate.setNomeProduto(linhaTemp[2]);
                        produtoDTOUpdate.setPrecoProduto(Double.parseDouble(linhaTemp[3]));
                        produtoDTOUpdate.setUnidadeProduto(Integer.parseInt(linhaTemp[4]));
                        produtoDTOUpdate.setPesoUnidade(Double.parseDouble(linhaTemp[5]));
                        produtoDTOUpdate.setValidadeProduto(linhaTemp[6]);
                        produtoDTOUpdate.setLinhaCategoria(Long.parseLong(linhaTemp[7]));
                        this.update(produtoDTOUpdate, produtoDTOUpdate.getIdProduto());

                    } else {

                        LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(Long.parseLong(linhaTemp[7]));

                        Produto produto = new Produto(linhaTemp[1], linhaTemp[2],
                                Double.parseDouble(linhaTemp[3]), Integer.parseInt(linhaTemp[4]),
                                Double.parseDouble(linhaTemp[5]), linhaTemp[6], linhaCategoria);

                        this.iProdutoRepository.save(produto);
                    }
                }

            }

    }

    public boolean checkFornecedorFromProduto(Long idLinha, Long idFornecedorToCheck) {
        boolean isSameFornecedor = false;
        LinhaCategoria linhaCategoria = linhaCategoriaService.findLinhaById(idLinha);
        isSameFornecedor = linhaCategoria.getCategoriaProduto().getFornecedor().getId() == idFornecedorToCheck;
        return isSameFornecedor;
    }
}
