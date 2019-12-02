package br.com.hbsis.funcionario;

import com.microsoft.sqlserver.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);

    private final IFuncionarioRepository iFuncionarioRepository;

    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository) {
        this.iFuncionarioRepository = iFuncionarioRepository;
    }

    public FuncionarioDTO save(FuncionarioDTO funcionarioDTO) {
        this.validate(funcionarioDTO);

        LOGGER.info("Salvando Funcionario");
        LOGGER.debug("Fornecedor: {}", funcionarioDTO);

        Funcionario funcionario = new Funcionario(
                funcionarioDTO.getNome(),
                funcionarioDTO.getEmail());

        funcionario = this.iFuncionarioRepository.save(funcionario);

        return FuncionarioDTO.of(funcionario);

    }

    private void validate(FuncionarioDTO funcionarioDTO) {
        LOGGER.info("Validando Fornecedor");

        if (funcionarioDTO == null) {
            throw new IllegalArgumentException("FuncionarioDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(funcionarioDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulao/vazio");
        }

        if (StringUtils.isEmpty(funcionarioDTO.getEmail())) {
            throw new IllegalArgumentException("E-mail não deve ser nulo/vazio");
        }

    }

    public FuncionarioDTO findById(Long id) {
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioOptional.isPresent()) {
            return FuncionarioDTO.of(funcionarioOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public List<Funcionario> findAll() {

        List<Funcionario> funcionarios = iFuncionarioRepository.findAll();

        return funcionarios;
    }

    public FuncionarioDTO update(FuncionarioDTO funcionarioDTO, Long id) {
        Optional<Funcionario> funcionarioxistenteOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioxistenteOptional.isPresent()) {
            Funcionario funcionarioExistente = funcionarioxistenteOptional.get();

            LOGGER.info("Atualizando fornecedor... id: [{}]", funcionarioExistente.getId());
            LOGGER.debug("Payload: {}", funcionarioDTO);
            LOGGER.debug("Usuario Existente: {}", funcionarioExistente);

            funcionarioExistente.setNome(funcionarioDTO.getNome());
            funcionarioExistente.setEmail(funcionarioDTO.getEmail());


            funcionarioExistente = this.iFuncionarioRepository.save(funcionarioExistente);

            return funcionarioDTO.of(funcionarioExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para funcionario de ID: [{}]", id);

        this.iFuncionarioRepository.deleteById(id);
    }
}
