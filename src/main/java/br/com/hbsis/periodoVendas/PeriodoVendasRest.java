package br.com.hbsis.periodoVendas;

import br.com.hbsis.fornecedor.FornecedorService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periodoVendas")
public class PeriodoVendasRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendas.class);

    private final PeriodoVendasService periodoVendasService;

    private final FornecedorService fornecedorService;

    @Autowired
    public PeriodoVendasRest(PeriodoVendasService periodoVendasService, FornecedorService fornecedorService) {
        this.periodoVendasService = periodoVendasService;
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public PeriodoVendasDTO save(@RequestBody PeriodoVendasDTO periodoVendasDTO){

        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", periodoVendasDTO);

        return  this.periodoVendasService.save(periodoVendasDTO);
    }

    @GetMapping("/{id}")
    public PeriodoVendasDTO find(@PathVariable("id") Long id){
        LOGGER.info("Recebendo Periodo de vendas de ID: [{}]", id);

        return this.periodoVendasService.findById(id);
    }

    @RequestMapping("/all")
    public List<PeriodoVendas> findAll(){
        List<PeriodoVendas> pv = periodoVendasService.findAll();
        return  pv;
    }

    @PutMapping("/{id}")
    public PeriodoVendasDTO update(@PathVariable("id") Long id, @RequestBody PeriodoVendasDTO periodoVendasDTO){
        LOGGER.info("Recebendo Update para produto de ID: {}", id);
        LOGGER.debug("Payload: {}", periodoVendasDTO);

        return  this.periodoVendasService.update(periodoVendasDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        this.periodoVendasService.delete(id);
    }



}
