package br.com.hbsis.periodoVendas;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.util.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class PeriodoVendasService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendas.class);

    private final IPeriodoVendasRepository iPeriodoVendasRepository;
    private final FornecedorService fornecedorService;
    private final DateValidator dateValidator;

    public PeriodoVendasService(IPeriodoVendasRepository iPeriodoVendasRepository, FornecedorService fornecedorService, DateValidator dateValidator) {
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
        this.fornecedorService = fornecedorService;
        this.dateValidator = dateValidator;
    }

    public PeriodoVendasDTO save(PeriodoVendasDTO periodoVendasDTO) {


        Fornecedor fornecedor = fornecedorService.findFornecedorById(periodoVendasDTO.getIdFornecedor());


        this.validate(periodoVendasDTO);
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", periodoVendasDTO);


        PeriodoVendas periodoVendas = new PeriodoVendas(
                periodoVendasDTO.getDataInicio(),
                periodoVendasDTO.getDataFinal(),
                periodoVendasDTO.getDataRetirada(),
                fornecedor
        );



        periodoVendas = this.iPeriodoVendasRepository.save(periodoVendas);

        return periodoVendasDTO.of(periodoVendas);
    }

    private void validate(PeriodoVendasDTO periodoVendasDTO) {
        LOGGER.info("Validando Linha de categoria");

        if (periodoVendasDTO == null) {
            throw new IllegalArgumentException("Periodo de venda não deve ser nulo");
        }

        if (dateValidator.isThisDateValid(periodoVendasDTO.getDataInicio().toString(), "dd/MM/yyyy")) {
            throw new IllegalArgumentException("Periodo tem que ter uma Data de inicio válida");
        }
        if (dateValidator.isThisDateValid(periodoVendasDTO.getDataFinal().toString(), "dd/MM/yyyy")) {
            throw new IllegalArgumentException("Periodo tem que ter uma Data final válida");
        }
        if (dateValidator.isThisDateValid(periodoVendasDTO.getDataRetirada().toString(), "dd/MM/yyyy")) {
            throw new IllegalArgumentException("Periodo tem que ter uma Data de retirada válida");
        }

    }

    public PeriodoVendasDTO findById(Long id) {

        Optional<PeriodoVendas> periodoVendas = this.iPeriodoVendasRepository.findById(id);

        if (periodoVendas.isPresent()) {
            return PeriodoVendasDTO.of(periodoVendas.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<PeriodoVendas> findAll() {

        List<PeriodoVendas> periodoVendas = iPeriodoVendasRepository.findAll();

        return periodoVendas;
    }

    public PeriodoVendasDTO update(PeriodoVendasDTO periodoVendasDTO, Long id) {
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findById(id);


        if (periodoVendasOptional.isPresent()) {
            PeriodoVendas periodoVendasExistente = periodoVendasOptional.get();
            Fornecedor fornecedor = fornecedorService.findFornecedorById(periodoVendasDTO.getIdFornecedor());


            LOGGER.info("Atualizando produto... id: [{}]", periodoVendasExistente.getId());
            LOGGER.debug("Payload: {}", periodoVendasDTO);
            LOGGER.debug("Produto Existente: {}", periodoVendasExistente);

            periodoVendasExistente.setDataInicio(periodoVendasDTO.getDataInicio());
            periodoVendasExistente.setDataFinal(periodoVendasDTO.getDataFinal());
            periodoVendasExistente.setDataRetirada(periodoVendasDTO.getDataRetirada());
            periodoVendasExistente.setFornecedor(fornecedor);

            periodoVendasExistente = this.iPeriodoVendasRepository.save(periodoVendasExistente);

            return periodoVendasDTO.of(periodoVendasExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID: [{}]", id);

        this.iPeriodoVendasRepository.deleteById(id);
    }

}
