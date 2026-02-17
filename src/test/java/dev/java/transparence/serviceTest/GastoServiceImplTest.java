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

import dev.java.transparence.dto.GastoRequestDTO;
import dev.java.transparence.dto.GastoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.Gasto;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.GastoRepository;
import dev.java.transparence.service.ContratoService;
import dev.java.transparence.service.GastoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class GastoServiceImplTest {

    @Mock
    private GastoRepository gastoRepository;
    @Mock
    private ContratoService contratoService;
    @InjectMocks
    private GastoServiceImpl gastoServiceImpl;

    private GastoRequestDTO criarGastoRequestDTO() {
        GastoRequestDTO dto = new GastoRequestDTO();
        dto.setContratoId(1L);
        dto.setDescricao("Teste");
        dto.setValor(BigDecimal.valueOf(100));
        dto.setDataGasto(LocalDate.now());
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

    private Gasto criarGastoEntity() {
        Gasto gasto = new Gasto(
                criarContratoAtivo(),
                "Teste",
                BigDecimal.valueOf(100),
                LocalDate.now());

        ReflectionTestUtils.setField(gasto, "id", 1L);

        return gasto;
    }

    @Test
    void deveIncluirGastoComSucesso() {

        GastoRequestDTO dto = criarGastoRequestDTO();
        Contrato contrato = criarContratoAtivo();

        when(contratoService.buscarContratoParaOperacoes(dto.getContratoId()))
                .thenReturn(contrato);

        when(gastoRepository.existsByContrato_IdAndDataGastoAndValor(
                dto.getContratoId(), dto.getDataGasto(), dto.getValor())).thenReturn(false);

        when(gastoRepository.save(any())).thenReturn(criarGastoEntity());

        GastoResponseDTO response = gastoServiceImpl.incluirGasto(dto);

        assertEquals(dto.getContratoId(), response.getContratoId());

        verify(contratoService).buscarContratoParaOperacoes(dto.getContratoId());
        verify(gastoRepository).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoContratoNaoEstaAtivo() {
        GastoRequestDTO dto = criarGastoRequestDTO();
        Contrato contrato = criarContratoAtivo();
        contrato.setStatus(StatusContrato.ENCERRADO);

        when(contratoService.buscarContratoParaOperacoes(dto.getContratoId()))
                .thenReturn(contrato);
        assertThrows(BusinessException.class, () -> gastoServiceImpl.incluirGasto(dto));

        verify(contratoService).buscarContratoParaOperacoes(dto.getContratoId());
        verify(gastoRepository, never()).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoGastoJaExiste() {
        GastoRequestDTO dto = criarGastoRequestDTO();
        Contrato contrato = criarContratoAtivo();

        when(contratoService.buscarContratoParaOperacoes(dto.getContratoId()))
                .thenReturn(contrato);
        when(gastoRepository.existsByContrato_IdAndDataGastoAndValor(
                dto.getContratoId(), dto.getDataGasto(), dto.getValor())).thenReturn(true);
        assertThrows(BusinessException.class, () -> gastoServiceImpl.incluirGasto(dto));

        verify(contratoService).buscarContratoParaOperacoes(dto.getContratoId());
        verify(gastoRepository, never()).save(any());
    }

    @Test
    void deveAtualizarGastoComSucesso() {
        GastoRequestDTO dto = criarGastoRequestDTO();
        Gasto gasto = criarGastoEntity();

        when(gastoRepository.findById(gasto.getId())).thenReturn(Optional.of(gasto));
        when(gastoRepository.save(any())).thenReturn(gasto);

        GastoResponseDTO response = gastoServiceImpl.atualizarGasto(gasto.getId(), dto);

        assertEquals(gasto.getContrato().getId(), response.getContratoId());
        assertEquals(dto.getDescricao(), response.getDescricao());
        assertEquals(dto.getValor(), response.getValor());
        assertEquals(dto.getDataGasto(), response.getData());

        verify(gastoRepository).findById(gasto.getId());
        verify(gastoRepository).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoContratoNaoEstaAtivoAoAtualizarGasto() {
        GastoRequestDTO dto = criarGastoRequestDTO();
        Gasto gasto = criarGastoEntity();
        gasto.getContrato().setStatus(StatusContrato.ENCERRADO);

        when(gastoRepository.findById(gasto.getId())).thenReturn(Optional.of(gasto));

        assertThrows(BusinessException.class, () -> gastoServiceImpl.atualizarGasto(gasto.getId(), dto));

        verify(gastoRepository).findById(gasto.getId());
        verify(gastoRepository, never()).save(any());
    }

    @Test
    void deveRetornarExceptionQuandoGastoNaoEncontradoAoAtualizarGasto() {
        GastoRequestDTO dto = criarGastoRequestDTO();

        when(gastoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> gastoServiceImpl.atualizarGasto(dto.getContratoId(), dto));

        verify(gastoRepository).findById(dto.getContratoId());
        verify(gastoRepository, never()).save(any());
    }

    @Test
    void deveExcluirGastoComSucesso() {
        Gasto gasto = criarGastoEntity();
        when(gastoRepository.findById(gasto.getId())).thenReturn(Optional.of(gasto));
        gastoServiceImpl.excluirGasto(gasto.getId());
        verify(gastoRepository).findById(gasto.getId());
        verify(gastoRepository).deleteById(gasto.getId());
    }

    @Test
    void deveRetornarExceptionQuandoContratoNaoEstaAtivoAoExcluirGasto() {
        Gasto gasto = criarGastoEntity();
        gasto.getContrato().setStatus(StatusContrato.ENCERRADO);
        when(gastoRepository.findById(gasto.getId())).thenReturn(Optional.of(gasto));
        assertThrows(BusinessException.class, () -> gastoServiceImpl.excluirGasto(gasto.getId()));
        verify(gastoRepository).findById(gasto.getId());
        verify(gastoRepository, never()).deleteById(any());
    }

    @Test
    void deveRetornarExceptionQuandoGastoNaoEncontradoAoExcluirGasto() {
        when(gastoRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> gastoServiceImpl.excluirGasto(1L));
        verify(gastoRepository).findById(1L);
        verify(gastoRepository, never()).deleteById(any());
    }

    @Test
    void deveRetornarListaDeGastosComSucesso() {
        List<Gasto> gastos = new ArrayList<>();
        when(gastoRepository.findAll()).thenReturn(gastos);
        List<GastoResponseDTO> response = gastoServiceImpl.buscarTodosGastos();
        assertEquals(gastos.size(), response.size());
        verify(gastoRepository).findAll();
    }

    @Test
    void deveRetornarGastoPorIdComSucesso() {
        Gasto gasto = criarGastoEntity();
        when(gastoRepository.findById(gasto.getId())).thenReturn(Optional.of(gasto));
        GastoResponseDTO response = gastoServiceImpl.buscarGastoPorId(gasto.getId());
        assertEquals(gasto.getId(), response.getId());
        verify(gastoRepository).findById(gasto.getId());
    }

    @Test
    void deveRetornarExceptionQuandoGastoNaoEncontradoAoBuscarGastoPorId() {
        when(gastoRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> gastoServiceImpl.buscarGastoPorId(1L));
        verify(gastoRepository).findById(any());
    }
}
