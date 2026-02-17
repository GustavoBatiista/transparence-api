package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import dev.java.transparence.dto.UsuarioRequestDTO;
import dev.java.transparence.dto.UsuarioResponseDTO;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.UsuarioRepository;
import dev.java.transparence.service.UsuarioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioServiceImpl;

    private UsuarioRequestDTO criarUsuarioRequestDTO() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setCpf("12345678901");
        dto.setEmail("teste@teste.com");
        dto.setNome("Teste");
        dto.setSenha("123456");
        dto.setTelefone("12345678901");
        dto.setEndereco("Rua Teste, 123");
        dto.setCidade("Cidade Teste");
        dto.setEstado("SP");
        dto.setCep("12345678");
        return dto;
    }

    private Usuario criarUsuarioEntity() {
        return new Usuario(
                "12345678901",
                "Nome Antigo",
                "emailantigo@teste.com",
                "123456",
                "12345678901",
                "Rua Antiga, 123",
                "Cidade Antiga",
                "SP",
                "12345678");
    }

    @Test
    public void deveIncluirUsuarioComSucesso() {

        UsuarioRequestDTO dto = criarUsuarioRequestDTO();

        when(usuarioRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any()))
                .thenReturn(new Usuario(dto.getCpf(), dto.getNome(), dto.getEmail(), dto.getSenha(), dto.getTelefone(),
                        dto.getEndereco(), dto.getCidade(), dto.getEstado(), dto.getCep()));

        UsuarioResponseDTO response = usuarioServiceImpl.incluirUsuario(dto);
        assertEquals(dto.getCpf(), response.getCpf());
        assertEquals(dto.getNome(), response.getNome());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getTelefone(), response.getTelefone());
        assertEquals(dto.getEndereco(), response.getEndereco());
        assertEquals(dto.getCidade(), response.getCidade());
        assertEquals(dto.getEstado(), response.getEstado());
        assertEquals(dto.getCep(), response.getCep());

        verify(usuarioRepository, times(1)).existsByCpf(dto.getCpf());
        verify(usuarioRepository, times(1)).existsByEmail(dto.getEmail());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    public void deveAcusarCpfJaCadastrado() {

        UsuarioRequestDTO dto = criarUsuarioRequestDTO();

        when(usuarioRepository.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> usuarioServiceImpl.incluirUsuario(dto));

        verify(usuarioRepository, times(1)).existsByCpf(dto.getCpf());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveAcusarEmailJaCadastrado() {

        UsuarioRequestDTO dto = criarUsuarioRequestDTO();

        when(usuarioRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> usuarioServiceImpl.incluirUsuario(dto));

        verify(usuarioRepository).existsByCpf(dto.getCpf());
        verify(usuarioRepository, times(1)).existsByEmail(dto.getEmail());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveAtualizarUsuarioComSucesso() {
        Long id = 1L;

        UsuarioRequestDTO dto = criarUsuarioRequestDTO();
        
        dto.setNome("Nome Atualizado");
        dto.setEmail("novoemail@teste.com");
        dto.setTelefone("98765432100");
        dto.setEndereco("Nova Rua, 456");
        dto.setCidade("Nova Cidade");
        dto.setEstado("RJ");
        dto.setCep("87654321");

        Usuario usuario = criarUsuarioEntity();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));


        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);


        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO response = usuarioServiceImpl.atualizarUsuario(id, dto);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(dto.getNome(), response.getNome());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getTelefone(), response.getTelefone());
        assertEquals(dto.getEndereco(), response.getEndereco());
        assertEquals(dto.getCidade(), response.getCidade());
        assertEquals(dto.getEstado(), response.getEstado());
        assertEquals(dto.getCep(), response.getCep());
        assertEquals(usuario.getCpf(), response.getCpf()); // CPF não muda

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, times(1)).existsByEmail(dto.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void deveAtualizarUsuarioSemAlterarEmail() {
        Long id = 1L;

        UsuarioRequestDTO dto = criarUsuarioRequestDTO();

        Usuario usuario = criarUsuarioEntity();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO response = usuarioServiceImpl.atualizarUsuario(id, dto);
        assertNotNull(response);
        assertEquals(dto.getNome(), response.getNome());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getTelefone(), response.getTelefone());
        assertEquals(dto.getEndereco(), response.getEndereco());
        assertEquals(dto.getCidade(), response.getCidade());
        assertEquals(dto.getEstado(), response.getEstado());
        assertEquals(dto.getCep(), response.getCep());
        assertEquals(usuario.getCpf(), response.getCpf());

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, times(1)).existsByEmail(dto.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoEncontradoAoAtualizar() {

        Long id = 1L;
        UsuarioRequestDTO dto = criarUsuarioRequestDTO();

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> usuarioServiceImpl.atualizarUsuario(id, dto));

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveLancarExcecaoAoTentarAtualizarUsuarioComEmailJaCadastradoEmOutroUsuario() {
        Long id = 1L;

        UsuarioRequestDTO dto = criarUsuarioRequestDTO();
        dto.setNome("Nome Atualizado");
        dto.setEmail("novoemail@teste.com");
        dto.setTelefone("98765432100");
        dto.setEndereco("Nova Rua, 456");
        dto.setCidade("Nova Cidade");
        dto.setEstado("RJ");
        dto.setCep("87654321");

        Usuario usuarioExistente = criarUsuarioEntity();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));

       
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> usuarioServiceImpl.atualizarUsuario(id, dto));

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).existsByEmail(dto.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    public void deveExcluirUsuarioComSucesso() {
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(true);
        usuarioServiceImpl.excluirUsuario(id);
        verify(usuarioRepository, times(1)).existsById(id);
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    @Test
    public void deveLancarExcecaoAoTentarExcluirUsuarioNaoExistente() {
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(false);
        assertThrows(NotFoundException.class,
                () -> usuarioServiceImpl.excluirUsuario(id));
        verify(usuarioRepository, times(1)).existsById(id);
        verify(usuarioRepository, never()).deleteById(id);
    }

    @Test
    public void deveBuscarUsuarioPorIdComSucesso() {

        Long id = 1L;
 
        Usuario usuario = criarUsuarioEntity();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = usuarioServiceImpl.buscarUsuarioPorId(id);

        assertEquals(usuario.getCpf(), response.getCpf());

        verify(usuarioRepository).findById(id);
    }

    @Test
    public void deveLancarExcecaoAoBuscarUsuarioPorIdNaoExistente() {
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> usuarioServiceImpl.buscarUsuarioPorId(id));
        verify(usuarioRepository, times(1)).findById(id);
    }
}
