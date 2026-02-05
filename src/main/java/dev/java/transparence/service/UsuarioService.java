package dev.java.transparence.service;

import org.springframework.stereotype.Service;

import dev.java.transparence.dto.UsuarioRequestDTO;
import dev.java.transparence.dto.UsuarioResponseDTO;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO incluirUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario(dto.getCpf(), dto.getNome(), dto.getEmail(), dto.getSenha(),
                dto.getTelefone(), dto.getEndereco(), dto.getCidade(), dto.getEstado(),
                dto.getCep());

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario existente = buscarUsuarioEntityPorId(id);
        if (usuarioRepository.existsByEmail(dto.getEmail()) && !existente.getEmail().equals(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setTelefone(dto.getTelefone());
        existente.setEndereco(dto.getEndereco());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setCep(dto.getCep());
        return toResponseDTO(usuarioRepository.save(existente));
    }

    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarUsuarioEntityPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        return toResponseDTO(buscarUsuarioEntityPorId(id));
                
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setCpf(usuario.getCpf());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setEndereco(usuario.getEndereco());
        dto.setCidade(usuario.getCidade());
        dto.setEstado(usuario.getEstado());
        dto.setCep(usuario.getCep());

        return dto;
    }
}