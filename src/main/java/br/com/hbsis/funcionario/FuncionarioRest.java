package br.com.hbsis.funcionario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Funcionario.class);

    private final FuncionarioService funcionarioService;

    @Autowired
    public FuncionarioRest(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public FuncionarioDTO save(@RequestBody FuncionarioDTO funcionarioDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Funcionario...");
        LOGGER.debug("Payaload: {}", funcionarioDTO);
        return this.funcionarioService.save(funcionarioDTO);
    }

    @GetMapping("/{id}")
    public FuncionarioDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.funcionarioService.findById(id);
    }

    @RequestMapping("/all")
    public List<Funcionario> findFuncionarios() {

        List<Funcionario> funcionarios = funcionarioService.findAll();
        return funcionarios;
    }

    @PutMapping("/{id}")
    public FuncionarioDTO udpate(@PathVariable("id") Long id, @RequestBody FuncionarioDTO funcionarioDTO) {
        LOGGER.info("Recebendo Update para funcionario de ID: {}", id);
        LOGGER.debug("Payload: {}", funcionarioDTO);

        return this.funcionarioService.update(funcionarioDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para funcionario de ID: {}", id);

        this.funcionarioService.delete(id);
    }


}
