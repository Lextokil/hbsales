package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStreamReader;
import java.util.*;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private  final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;

    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

        categoriaProdutoDTO.setFornecedor(fornecedorService.findFornecedorById((categoriaProdutoDTO.getFornecedor().getId())));

        this.validate(categoriaProdutoDTO);
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", categoriaProdutoDTO);
        LOGGER.debug("Fornecedor", categoriaProdutoDTO.getFornecedor().getNome());


        CategoriaProduto categoriaProduto = new CategoriaProduto(
                    categoriaProdutoDTO.getCodCategoria(),
                    categoriaProdutoDTO.getNome(),
                    categoriaProdutoDTO.getFornecedor());



        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);

        return categoriaProdutoDTO.of(categoriaProduto);
    }

    private void validate(CategoriaProdutoDTO categoriaProdutoDTO) {
        LOGGER.info("Validando Produto");

        if (categoriaProdutoDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getCodCategoria())){
            throw new IllegalArgumentException("Codigo da categoria não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getFornecedor().toString())) {
            throw new IllegalArgumentException("Fornecedor não deve ser nulo/vazio");
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

        List<CategoriaProduto> categoriaProdutos =  iCategoriaProdutoRepository.findAll();

        return categoriaProdutos;
    }

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {
        Optional<CategoriaProduto> produtoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);


        if (produtoExistenteOptional.isPresent()) {
            CategoriaProduto categoriaProdutoExistente = produtoExistenteOptional.get();
            categoriaProdutoDTO.setFornecedor(fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedor().getId()));

            LOGGER.info("Atualizando produto... id: [{}]", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Produto Existente: {}", categoriaProdutoExistente);

            categoriaProdutoExistente.setCodCategoria(categoriaProdutoDTO.getCodCategoria());
            categoriaProdutoExistente.setNome(categoriaProdutoDTO.getNome());
            categoriaProdutoExistente.setFornecedor(categoriaProdutoDTO.getFornecedor());

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return CategoriaProdutoDTO.of(categoriaProdutoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID: [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
    }


    public boolean saveDataFromUploadFile(MultipartFile file) {
        boolean isFlag = false;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
     if (extension.equalsIgnoreCase("csv")){
            isFlag = readDataFromCsv(file);
        }

        return isFlag;
    }

    private boolean readDataFromCsv(MultipartFile file) {
        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

            List<String[]> linhas = csvReader.readAll();


            for(String[] linha : linhas){
                String[] linhaTemp = linha[0].split(";");


                Fornecedor fornecedor = fornecedorService.findFornecedorById(Long.parseLong(linhaTemp[2]));

                iCategoriaProdutoRepository.save(new CategoriaProduto(linhaTemp[0], linhaTemp[1],fornecedor));
            }
            return  true;
        }catch (Exception e){
            LOGGER.info(e.toString());
            return false;
        }
    }

}
