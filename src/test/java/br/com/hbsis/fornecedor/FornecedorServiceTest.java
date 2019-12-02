package br.com.hbsis.fornecedor;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FornecedorServiceTest {

    private MockMvc mockMvc;

    @Mock
    private IFornecedorRepository iFornecedorRepository;

    @Captor
    private ArgumentCaptor<Fornecedor> argumentCaptor;

    @InjectMocks
    private FornecedorService fornecedorService;

    @Test
    public void save() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO("razaoSocial", "cnpj", "nome", "endereco", "telefone",  "email");

        Fornecedor fornecedorMock = Mockito.mock(Fornecedor.class);

        when(fornecedorMock.getRazaoSocial()).thenReturn(fornecedorDTO.getRazaoSocial());
        when(fornecedorMock.getCnpj()).thenReturn(fornecedorDTO.getCnpj());
        when(fornecedorMock.getNome()).thenReturn(fornecedorDTO.getNome());
        when(fornecedorMock.getEndereco()).thenReturn(fornecedorDTO.getEndereco());
        when(fornecedorMock.getTelefone()).thenReturn(fornecedorDTO.getTelefone());
        when(fornecedorMock.getEmail()).thenReturn(fornecedorDTO.getEmail());

        when(this.iFornecedorRepository.save(any())).thenReturn(fornecedorMock);

        this.fornecedorService.save(fornecedorDTO);

        verify(this.iFornecedorRepository, times(1)).save(this.argumentCaptor.capture());
        Fornecedor createdFornecedor = argumentCaptor.getValue();

        assertTrue(StringUtils.isNoneEmpty(createdFornecedor.getRazaoSocial()), "Razao social não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdFornecedor.getCnpj()), "cnpj não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdFornecedor.getNome()), "Nome não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdFornecedor.getEndereco()), "Endereço não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdFornecedor.getTelefone()), "Telefone não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdFornecedor.getEmail()), "email não deve ser nulo");

    }

    @Test
    public void fornecedorDTONull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.fornecedorService.save(null);
        });
    }
    @Test
    public void fornecedorComEnderecoNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            FornecedorDTO fornecedorDTO = new FornecedorDTO("razaoSocial", "cnpj", "nome", null, "telefone",  "email");
            this.fornecedorService.save(fornecedorDTO);
        });
    }

    @Test
    public void fornecedorComCpnjNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            FornecedorDTO fornecedorDTO = new FornecedorDTO("razaoSocial", null, "nome", "endereco", "telefone",  "email");
            this.fornecedorService.save(fornecedorDTO);
        });
    }


}
