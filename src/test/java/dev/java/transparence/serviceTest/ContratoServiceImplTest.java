package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.java.transparence.dto.ContratoRequestDTO;
import dev.java.transparence.dto.ContratoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.ContratoRepository;
import dev.java.transparence.service.ContratoServiceImpl;
import dev.java.transparence.service.PessoaCuidadaService;
import dev.java.transparence.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class ContratoServiceImplTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PessoaCuidadaService pessoaCuidadaService;

    @InjectMocks
    private ContratoServiceImpl contratoServiceImpl;

    private ContratoRequestDTO criarContratoRequestDTO() {
        ContratoRequestDTO dto = new ContratoRequestDTO();
        dto.setUsuarioId(1L);
        dto.setPessoaCuidadaId(1L);
        dto.setDataInicio(LocalDate.now());
        return dto;
    }

    private Contrato criarContratoEntity() {

        Contrato contrato = new Contrato(
                criarUsuario(),
                criarPessoaCuidada(),
                LocalDate.now());

        ReflectionTestUtils.setField(contrato, "id", 1L);
        ReflectionTestUtils.setField(contrato, "status", StatusContrato.ATIVO);

        return contrato;
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

    private void mockSaveComId() {
        when(contratoRepository.save(any()))
                .thenAnswer(invocation -> {
                    Contrato c = invocation.getArgument(0);
                    ReflectionTestUtils.setField(c, "id", 1L);
                    return c;
                });
    }

    @Test
    public void deveIncluirContratoComSucesso() {
        ContratoRequestDTO dto = criarContratoRequestDTO();
        Usuario usuario = criarUsuario();
        PessoaCuidada pessoaCuidada = criarPessoaCuidada();

        when(usuarioService.buscarUsuarioParaContrato(dto.getUsuarioId()))
                .thenReturn(usuario);

        when(pessoaCuidadaService.buscarPessoaCuidadaParaContrato(dto.getPessoaCuidadaId()))
                .thenReturn(pessoaCuidada);

        when(contratoRepository.existsByUsuario_IdAndPessoaCuidada_IdAndStatus(usuario.getId(),
                pessoaCuidada.getId(), StatusContrato.ATIVO)).thenReturn(false);

        mockSaveComId();

        ContratoResponseDTO response = contratoServiceImpl.incluirContrato(dto);

        assertEquals(1L, response.getId());
        assertEquals(dto.getUsuarioId(), response.getUsuarioId());
        assertEquals(dto.getPessoaCuidadaId(), response.getPessoaCuidadaId());
        assertEquals(dto.getDataInicio(), response.getDataInicio());
        assertEquals(StatusContrato.ATIVO, response.getStatus());

        verify(usuarioService).buscarUsuarioParaContrato(dto.getUsuarioId());
        verify(pessoaCuidadaService).buscarPessoaCuidadaParaContrato(dto.getPessoaCuidadaId());
        verify(contratoRepository).existsByUsuario_IdAndPessoaCuidada_IdAndStatus(dto.getUsuarioId(),
                dto.getPessoaCuidadaId(), StatusContrato.ATIVO);
        verify(contratoRepository).save(any(Contrato.class));
    }

    @Test
    public void deveRetornarExceptionQuandoContratoJaEstaCadastrado() {
        ContratoRequestDTO dto = criarContratoRequestDTO();
        Usuario usuario = criarUsuario();
        PessoaCuidada pessoaCuidada = criarPessoaCuidada();
        when(usuarioService.buscarUsuarioParaContrato(dto.getUsuarioId())).thenReturn(usuario);
        when(pessoaCuidadaService.buscarPessoaCuidadaParaContrato(dto.getPessoaCuidadaId())).thenReturn(pessoaCuidada);

        when(contratoRepository.existsByUsuario_IdAndPessoaCuidada_IdAndStatus(usuario.getId(),
                pessoaCuidada.getId(), StatusContrato.ATIVO)).thenReturn(true);

        assertThrows(BusinessException.class, () -> contratoServiceImpl.incluirContrato(dto));

        verify(usuarioService).buscarUsuarioParaContrato(dto.getUsuarioId());
        verify(pessoaCuidadaService).buscarPessoaCuidadaParaContrato(dto.getPessoaCuidadaId());
        verify(contratoRepository).existsByUsuario_IdAndPessoaCuidada_IdAndStatus(usuario.getId(),
                pessoaCuidada.getId(), StatusContrato.ATIVO);
        verify(contratoRepository, never()).save(any(Contrato.class));
    }

    @Test
    public void deveEncerrarContratoComSucesso() {

        Contrato contrato = criarContratoEntity();

        when(contratoRepository.findById(contrato.getId())).thenReturn(Optional.of(contrato));

        mockSaveComId();

        ContratoResponseDTO response = contratoServiceImpl.encerrarContrato(contrato.getId());

        assertEquals(contrato.getId(), response.getId());
        assertEquals(StatusContrato.ENCERRADO, response.getStatus());
        assertEquals(LocalDate.now(), response.getDataFim());

        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository).save(any(Contrato.class));
    }

    @Test
    public void deveRetornarExceptionQuandoContratoJaEstaEncerrado() {

        Contrato contrato = criarContratoEntity();
        contrato.setStatus(StatusContrato.ENCERRADO);

        when(contratoRepository.findById(contrato.getId())).thenReturn(Optional.of(contrato));

        assertThrows(BusinessException.class, () -> contratoServiceImpl.encerrarContrato(contrato.getId()));

        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository, never()).save(any(Contrato.class));
    }

    @Test
    public void deveSuspenderContratoComSucesso() {

        Contrato contrato = criarContratoEntity();

        when(contratoRepository.findById(contrato.getId())).thenReturn(Optional.of(contrato));
        mockSaveComId();

        ContratoResponseDTO response = contratoServiceImpl.suspenderContrato(contrato.getId());

        assertEquals(contrato.getId(), response.getId());
        assertEquals(StatusContrato.SUSPENSO, response.getStatus());

        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository).save(any(Contrato.class));
    }

    @Test
    public void deveRetornarExceptionQuandoContratoJaEstaSuspensoOuEncerrado() {

        Contrato contrato = criarContratoEntity();
        contrato.setStatus(StatusContrato.SUSPENSO);

        when(contratoRepository.findById(contrato.getId()))
                .thenReturn(Optional.of(contrato));

        assertThrows(BusinessException.class,
                () -> contratoServiceImpl.suspenderContrato(contrato.getId()));

        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository, never()).save(any());
    }

    @Test
    public void deveReativarContratoComSucesso() {

        Contrato contrato = criarContratoEntity();

        ReflectionTestUtils.setField(contrato, "status", StatusContrato.SUSPENSO);

        when(contratoRepository.findById(contrato.getId())).thenReturn(Optional.of(contrato));
        mockSaveComId();
        ContratoResponseDTO response = contratoServiceImpl.reativarContrato(contrato.getId());

        assertEquals(contrato.getId(), response.getId());
        assertEquals(StatusContrato.ATIVO, response.getStatus());

        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository).save(any(Contrato.class));
    }

    @Test
    public void deveRetornarExceptionQuandoContratoNaoEstaSuspenso() {
        Contrato contrato = criarContratoEntity();

        when(contratoRepository.findById(contrato.getId()))
                .thenReturn(Optional.of(contrato));

        assertThrows(BusinessException.class,
                () -> contratoServiceImpl.reativarContrato(contrato.getId()));

        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository, never()).save(any());
    }

    @Test
    public void deveExcluirContratoComSucesso() {
        Contrato contrato = criarContratoEntity();

        when(contratoRepository.existsById(contrato.getId())).thenReturn(true);

        contratoServiceImpl.excluirContrato(contrato.getId());

        verify(contratoRepository).existsById(contrato.getId());
        verify(contratoRepository).deleteById(contrato.getId());
    }

    @Test
    public void deveRetornarExceptionQuandoContratoNaoExiste() {
        Contrato contrato = criarContratoEntity();
        when(contratoRepository.existsById(contrato.getId())).thenReturn(false);
        assertThrows(NotFoundException.class,
                () -> contratoServiceImpl.excluirContrato(contrato.getId()));
        verify(contratoRepository).existsById(contrato.getId());
        verify(contratoRepository, never()).deleteById(contrato.getId());
    }

    @Test
    public void deveBuscarContratoPorIdComSucesso() {
        Contrato contrato = criarContratoEntity();
        when(contratoRepository.findById(contrato.getId())).thenReturn(Optional.of(contrato));
        ContratoResponseDTO response = contratoServiceImpl.buscarContratoPorId(contrato.getId());
        assertEquals(contrato.getId(), response.getId());
        assertEquals(contrato.getUsuario().getId(), response.getUsuarioId());
        assertEquals(contrato.getPessoaCuidada().getId(), response.getPessoaCuidadaId());
        assertEquals(contrato.getDataInicio(), response.getDataInicio());
        assertEquals(contrato.getStatus(), response.getStatus());

        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository, never()).save(any(Contrato.class));
    }

    @Test
    public void deveRetornarExceptionQuandoContratoNaoExisteAoBuscarPorId() {
        Contrato contrato = criarContratoEntity();
        when(contratoRepository.findById(contrato.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> contratoServiceImpl.buscarContratoPorId(contrato.getId()));
        verify(contratoRepository).findById(contrato.getId());
        verify(contratoRepository, never()).save(any(Contrato.class));
    }
}
