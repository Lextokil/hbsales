package br.com.hbsis.fornecedor;

import br.com.hbsis.validation.CnpjValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FornecedorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);

	private final IFornecedorRepository fornecedorRepository;

	@Autowired
	public FornecedorService(IFornecedorRepository fornecedorRepository) {
		this.fornecedorRepository = fornecedorRepository;
	}

	public FornecedorDTO save(FornecedorDTO fornecedorDTO) {
		return FornecedorDTO.of(this.saveEntity(fornecedorDTO));
	}

	private Fornecedor saveEntity(FornecedorDTO fornecedorDTO) {
		LOGGER.info("Sanvando fornecedor");
		LOGGER.debug("Payload: {}", fornecedorDTO);

		Fornecedor fornecedor = this.fromDto(fornecedorDTO, new Fornecedor());


		fornecedor = this.fornecedorRepository.save(fornecedor);

		LOGGER.trace("Fornecedor Salvo {}", fornecedor);

		return fornecedor;
	}

	private Fornecedor fromDto(FornecedorDTO fornecedorDTO, Fornecedor fornecedor) {

		fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
		fornecedor.setCnpj(fornecedorDTO.getCnpj());
		fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
		fornecedor.setEndereco(fornecedorDTO.getEndereco());
		fornecedor.setEmailContato(fornecedorDTO.getEmailContato());
		fornecedor.setTelefoneContato(fornecedorDTO.getTelefoneContato());

		return fornecedor;
	}

	public FornecedorDTO findById(Long id) {
		return FornecedorDTO.of(findFornecedorById(id));
	}

	public Fornecedor findFornecedorById(Long id) {
		Optional<Fornecedor> fornecedorOptional = this.fornecedorRepository.findById(id);

		if (fornecedorOptional.isPresent()) {
			return fornecedorOptional.get();
		}


		throw new NoSuchElementException(String.format("Id [%s] não existe...", id));
	}
	public List<FornecedorDTO> findAll() {

		List<Fornecedor> fornecedores = fornecedorRepository.findAll();
		List<FornecedorDTO> fornecedorDTO = new ArrayList<>();
		fornecedores.forEach(fornecedor -> fornecedorDTO.add(FornecedorDTO.of(fornecedor)));

		return fornecedorDTO;
	}

	public FornecedorDTO update(Long id, FornecedorDTO fornecedorDTO) {
		Fornecedor fornecedor = this.findFornecedorById(id);

		LOGGER.debug("Atualizando fornecedor....");
		LOGGER.debug("Fornecedor atual: {} / Fornecedor novo: {}", fornecedor, fornecedorDTO);

		fornecedor = this.fornecedorRepository.save(this.fromDto(fornecedorDTO, fornecedor));

		return FornecedorDTO.of(fornecedor);
	}
	public void delete(Long id) {
		LOGGER.info("Executando delete para fornecedor de ID: [{}]", id);

		this.fornecedorRepository.deleteById(id);
	}
}
