package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.java.transparence.dto.RecebimentoRequestDTO;
import dev.java.transparence.dto.RecebimentoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Recebimento;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.RecebimentoRepository;
import dev.java.transparence.service.ContratoService;
import dev.java.transparence.service.RecebimentoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RecebimentoServiceImplTest {

    @Mock
    private RecebimentoRepository recebimentoRepository;
    @Mock
    private ContratoService contratoService;
    @InjectMocks
    private RecebimentoServiceImpl recebimentoServiceImpl;

    private RecebimentoRequestDTO criarRecebimentoRequestDTO() {
        RecebimentoRequestDTO dto = new RecebimentoRequestDTO();
        dto.setContratoId(1L);
        dto.setDescricao("Teste");
        dto.setValor(BigDecimal.valueOf(100));
        dto.setDataRecebimento(LocalDate.now());
        return dto;
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario(
                "12345678901", "Teste", "teste@teste.com", "123456",
                "11999999999", "Rua", "Cidade", "SP", "12345678");

        ReflectionTestUtils.setField(usuario, "id", 1L);

        return usuario;
    }

    private PessoaCuidada criarPessoaCuidada() {
        PessoaCuidada pessoa = new PessoaCuidada(
                "12345678901", "Teste", "11999999999",
                "Rua", "Cidade", "SP", "12345678");

        ReflectionTestUtils.setField(pessoa, "id", 1L);

        return pessoa;
    }

    private Contrato criarContratoAtivo() {

        Contrato contrato = new Contrato(
                criarUsuario(),
                criarPessoaCuidada(),
                LocalDate.now());

        ReflectionTestUtils.setField(contrato, "id", 1L);
        ReflectionTestUtils.setField(contrato, "status", StatusContrato.ATIVO);

        return contrato;
    }

    private Recebimento criarRecebimentoEntity() {
        Recebimento recebimento = new Recebimento(
                criarContratoAtivo(),
                "Teste",
                BigDecimal.valueOf(100),
                LocalDate.now());

        ReflectionTestUtils.setField(recebimento, "id", 1L);

        return recebimento;
    }

    @Test
    void deveIncluirRecebimentoComSucesso() {

        RecebimentoRequestDTO dto = criarRecebimentoRequestDTO();
        Contrato contrato = criarContratoAtivo();

        when(contratoService.buscarContratoParaOperacoes(dto.getContratoId()))
                .thenReturn(contrato);

        when(recebimentoRepository.existsByContrato_IdAndDataRecebimentoAndValor(
                dto.getContratoId(), dto.getDataRecebimento(), dto.getValor())).thenReturn(false);

        when(recebimentoRepository.save(any())).thenReturn(criarRecebimentoEntity());

        RecebimentoResponseDTO response = recebimentoServiceImpl.incluirRecebimento(dto);

        assertEquals(dto.getContratoId(), response.getContratoId());
        assertEquals(dto.getDescricao(), response.getDescricao());
        assertEquals(dto.getValor(), response.getValor());
        assertEquals(dto.getDataRecebimento(), response.getData());

        verify(contratoService).buscarContratoParaOperacoes(dto.getContratoId());
        verify(recebimentoRepository).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoContratoNaoEstaAtivo() {
        RecebimentoRequestDTO dto = criarRecebimentoRequestDTO();
        Contrato contrato = criarContratoAtivo();
        contrato.setStatus(StatusContrato.ENCERRADO);

        when(contratoService.buscarContratoParaOperacoes(dto.getContratoId()))
                .thenReturn(contrato);
        assertThrows(BusinessException.class, () -> recebimentoServiceImpl.incluirRecebimento(dto));

        verify(contratoService).buscarContratoParaOperacoes(dto.getContratoId());
        verify(recebimentoRepository, never()).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoRecebimentoJaExiste() {
        RecebimentoRequestDTO dto = criarRecebimentoRequestDTO();
        Contrato contrato = criarContratoAtivo();

        when(contratoService.buscarContratoParaOperacoes(dto.getContratoId()))
                .thenReturn(contrato);
        when(recebimentoRepository.existsByContrato_IdAndDataRecebimentoAndValor(
                dto.getContratoId(), dto.getDataRecebimento(), dto.getValor())).thenReturn(true);
        assertThrows(BusinessException.class, () -> recebimentoServiceImpl.incluirRecebimento(dto));

        verify(contratoService).buscarContratoParaOperacoes(dto.getContratoId());
        verify(recebimentoRepository, never()).save(any());
    }

    @Test
    void deveAtualizarRecebimentoComSucesso() {
        RecebimentoRequestDTO dto = criarRecebimentoRequestDTO();
        Recebimento recebimento = criarRecebimentoEntity();

        when(recebimentoRepository.findById(recebimento.getId())).thenReturn(Optional.of(recebimento));
        when(recebimentoRepository.save(any())).thenReturn(recebimento);

        RecebimentoResponseDTO response = recebimentoServiceImpl.atualizarRecebimento(recebimento.getId(), dto);

        assertEquals(recebimento.getContrato().getId(), response.getContratoId());
        assertEquals(dto.getDescricao(), response.getDescricao());
        assertEquals(dto.getValor(), response.getValor());
        assertEquals(dto.getDataRecebimento(), response.getData());

        verify(recebimentoRepository).findById(recebimento.getId());
        verify(recebimentoRepository).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoContratoNaoEstaAtivoAoAtualizarRecebimento() {
        RecebimentoRequestDTO dto = criarRecebimentoRequestDTO();
        Recebimento recebimento = criarRecebimentoEntity();
        recebimento.getContrato().setStatus(StatusContrato.ENCERRADO);

        when(recebimentoRepository.findById(recebimento.getId())).thenReturn(Optional.of(recebimento));

        assertThrows(BusinessException.class,
                () -> recebimentoServiceImpl.atualizarRecebimento(recebimento.getId(), dto));

        verify(recebimentoRepository).findById(recebimento.getId());
        verify(recebimentoRepository, never()).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoRecebimentoNaoEncontradoAoAtualizarRecebimento() {
        RecebimentoRequestDTO dto = criarRecebimentoRequestDTO();

        when(recebimentoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> recebimentoServiceImpl.atualizarRecebimento(1L, dto));

        verify(recebimentoRepository).findById(1L);
        verify(recebimentoRepository, never()).save(any());
    }

    @Test
    void deveExcluirRecebimentoComSucesso() {
        Recebimento recebimento = criarRecebimentoEntity();
        when(recebimentoRepository.findById(recebimento.getId())).thenReturn(Optional.of(recebimento));
        recebimentoServiceImpl.excluirRecebimento(recebimento.getId());
        verify(recebimentoRepository).findById(recebimento.getId());
        verify(recebimentoRepository).deleteById(recebimento.getId());
    }

    @Test
    void deveRetornarExceptionQuandoContratoNaoEstaAtivoAoExcluirRecebimento() {
        Recebimento recebimento = criarRecebimentoEntity();
        recebimento.getContrato().setStatus(StatusContrato.ENCERRADO);
        when(recebimentoRepository.findById(recebimento.getId())).thenReturn(Optional.of(recebimento));
        assertThrows(BusinessException.class, () -> recebimentoServiceImpl.excluirRecebimento(recebimento.getId()));
        verify(recebimentoRepository).findById(recebimento.getId());
        verify(recebimentoRepository, never()).deleteById(any());
    }

    @Test
    void deveRetornarExceptionQuandoRecebimentoNaoEncontradoAoExcluirRecebimento() {
        when(recebimentoRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> recebimentoServiceImpl.excluirRecebimento(1L));
        verify(recebimentoRepository).findById(1L);
        verify(recebimentoRepository, never()).deleteById(any());
    }

    @Test
    void deveRetornarListaDeRecebimentosComSucesso() {
        List<Recebimento> recebimentos = new ArrayList<>();
        when(recebimentoRepository.findAll()).thenReturn(recebimentos);
        List<RecebimentoResponseDTO> response = recebimentoServiceImpl.buscarTodosRecebimentos();
        assertEquals(recebimentos.size(), response.size());
        verify(recebimentoRepository).findAll();
    }

    @Test
    void deveRetornarRecebimentoPorIdComSucesso() {
        Recebimento recebimento = criarRecebimentoEntity();
        when(recebimentoRepository.findById(recebimento.getId())).thenReturn(Optional.of(recebimento));
        RecebimentoResponseDTO response = recebimentoServiceImpl.buscarRecebimentoPorId(recebimento.getId());
        assertEquals(recebimento.getId(), response.getId());
        verify(recebimentoRepository).findById(recebimento.getId());
    }

    @Test
    void deveRetornarExceptionQuandoRecebimentoNaoEncontradoAoBuscarRecebimentoPorId() {
        when(recebimentoRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> recebimentoServiceImpl.buscarRecebimentoPorId(1L));
        verify(recebimentoRepository).findById(1L);
    }
}
