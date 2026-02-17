package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.java.transparence.dto.PessoaCuidadaRequestDTO;
import dev.java.transparence.dto.PessoaCuidadaResponseDTO;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.PessoaCuidadaRepository;
import dev.java.transparence.service.PessoaCuidadaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PessoaCuidadaServiceImplTest {

    @Mock
    private PessoaCuidadaRepository pessoaCuidadaRepository;

    @InjectMocks
    private PessoaCuidadaServiceImpl pessoaCuidadaServiceImpl;

    private PessoaCuidadaRequestDTO criarPessoaCuidadaRequestDTO() {
        PessoaCuidadaRequestDTO dto = new PessoaCuidadaRequestDTO();
        dto.setCpf("12345678901");
        dto.setNome("Teste");
        dto.setTelefone("12345678901");
        dto.setEndereco("Rua Teste, 123");
        dto.setCidade("Cidade Teste");
        dto.setEstado("SP");
        dto.setCep("12345678");
        return dto;
    }

    private PessoaCuidada criarPessoaCuidadaEntity() {
        return new PessoaCuidada("12345678901", "Teste", "12345678901", "Rua Teste, 123", "Cidade Teste", "SP",
                "12345678");
    }

    @Test
    public void deveIncluirPessoaCuidadaComSucesso() {
        PessoaCuidadaRequestDTO dto = criarPessoaCuidadaRequestDTO();
        PessoaCuidada pessoaCuidada = criarPessoaCuidadaEntity();

        when(pessoaCuidadaRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(pessoaCuidadaRepository.save(any(PessoaCuidada.class))).thenReturn(pessoaCuidada);

        PessoaCuidadaResponseDTO response = pessoaCuidadaServiceImpl.incluirPessoaCuidada(dto);
        assertEquals(dto.getNome(), response.getNome());

        verify(pessoaCuidadaRepository).existsByCpf(dto.getCpf());
        verify(pessoaCuidadaRepository).save(any(PessoaCuidada.class));

    }

    @Test
    public void deveLancarExcecaoAoIncluirPessoaCuidadaComCpfJaCadastrado() {
        PessoaCuidadaRequestDTO dto = criarPessoaCuidadaRequestDTO();

        when(pessoaCuidadaRepository.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThrows(BusinessException.class, () -> pessoaCuidadaServiceImpl.incluirPessoaCuidada(dto));

        verify(pessoaCuidadaRepository).existsByCpf(dto.getCpf());
        verify(pessoaCuidadaRepository, never()).save(any(PessoaCuidada.class));

    }

    @Test
    public void deveAtualizarPessoaCuidadaComSucesso() {
        Long id = 1L;
        PessoaCuidadaRequestDTO dto = criarPessoaCuidadaRequestDTO();
        PessoaCuidada pessoaCuidada = criarPessoaCuidadaEntity();
        when(pessoaCuidadaRepository.findById(id)).thenReturn(Optional.of(pessoaCuidada));
        when(pessoaCuidadaRepository.save(any(PessoaCuidada.class))).thenReturn(pessoaCuidada);
        PessoaCuidadaResponseDTO response = pessoaCuidadaServiceImpl.atualizarPessoaCuidada(id, dto);

        assertEquals(dto.getNome(), response.getNome());

        verify(pessoaCuidadaRepository).findById(id);
        verify(pessoaCuidadaRepository).save(any(PessoaCuidada.class));
    }

    @Test
    public void deveLancarExcecaoAoAtualizarPessoaCuidadaNaoEncontrada() {
        Long id = 1L;
        PessoaCuidadaRequestDTO dto = criarPessoaCuidadaRequestDTO();

        when(pessoaCuidadaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pessoaCuidadaServiceImpl.atualizarPessoaCuidada(id, dto));

        verify(pessoaCuidadaRepository).findById(id);
        verify(pessoaCuidadaRepository, never()).save(any(PessoaCuidada.class));
    }

    @Test
    public void deveExcluirPessoaCuidadaComSucesso() {
        Long id = 1L;
        when(pessoaCuidadaRepository.existsById(id)).thenReturn(true);
        pessoaCuidadaServiceImpl.excluirPessoaCuidada(id);
        verify(pessoaCuidadaRepository, times(1)).existsById(id);
        verify(pessoaCuidadaRepository, times(1)).deleteById(id);
    }

    @Test
    public void deveLancarExcecaoAoExcluirPessoaCuidadaNaoEncontrada() {
        Long id = 1L;
        when(pessoaCuidadaRepository.existsById(id)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> pessoaCuidadaServiceImpl.excluirPessoaCuidada(id));

        verify(pessoaCuidadaRepository).existsById(id);
        verify(pessoaCuidadaRepository, never()).deleteById(id);
    }

    @Test
    public void deveBuscarPessoaCuidadaPorIdComSucesso() {
        Long id = 1L;
        PessoaCuidada pessoaCuidada = criarPessoaCuidadaEntity();
        when(pessoaCuidadaRepository.findById(id)).thenReturn(Optional.of(pessoaCuidada));
        PessoaCuidadaResponseDTO response = pessoaCuidadaServiceImpl.buscarPessoaCuidadaPorId(id);

        assertEquals(pessoaCuidada.getCpf(), response.getCpf());

        verify(pessoaCuidadaRepository).findById(id);
    }

    @Test
    public void deveLancarExcecaoAoBuscarPessoaCuidadaPorIdNaoEncontrada() {
        Long id = 1L;
        when(pessoaCuidadaRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> pessoaCuidadaServiceImpl.buscarPessoaCuidadaPorId(id));
        verify(pessoaCuidadaRepository).findById(id);
    }

}
