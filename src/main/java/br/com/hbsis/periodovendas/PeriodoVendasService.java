package br.com.hbsis.periodovendas;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.util.DateValidator;
import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import org.apache.poi.ss.usermodel.DataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoVendasService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendas.class);

    private final IPeriodoVendasRepository iPeriodoVendasRepository;
    private final FornecedorService fornecedorService;

    public PeriodoVendasService(IPeriodoVendasRepository iPeriodoVendasRepository, FornecedorService fornecedorService, DateValidator dateValidator) {
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
        this.fornecedorService = fornecedorService;
    }

    public PeriodoVendasDTO save(PeriodoVendasDTO periodoVendasDTO) {


        Fornecedor fornecedor = fornecedorService.findFornecedorById(periodoVendasDTO.getIdFornecedor());


        this.validate(periodoVendasDTO);
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", periodoVendasDTO);


        PeriodoVendas periodoVendas = new PeriodoVendas(
                DateValidator.convertToLocalDateTime(periodoVendasDTO.getDataInicio()),
                DateValidator.convertToLocalDateTime(periodoVendasDTO.getDataFinal()),
                DateValidator.convertToLocalDateTime(periodoVendasDTO.getDataRetirada()),
                periodoVendasDTO.getDescricao(),
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

        if (DateValidator.isThisDateValid(periodoVendasDTO.getDataInicio().toString(), "dd-MM-yyyy")) {
            throw new IllegalArgumentException("Periodo tem que ter uma Data de inicio válida");
        }
        if (DateValidator.isThisDateValid(periodoVendasDTO.getDataFinal().toString(), "dd-MM-yyyy")) {
            throw new IllegalArgumentException("Periodo tem que ter uma Data final válida");
        }
        if (DateValidator.isThisDateValid(periodoVendasDTO.getDataRetirada().toString(), "dd-MM-yyyy")) {
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

            periodoVendasExistente.setDataInicio(DateValidator.convertToLocalDateTime(periodoVendasDTO.getDataInicio()));
            periodoVendasExistente.setDataFinal(DateValidator.convertToLocalDateTime(periodoVendasDTO.getDataFinal()));
            periodoVendasExistente.setDataRetirada(DateValidator.convertToLocalDateTime(periodoVendasDTO.getDataRetirada()));
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
