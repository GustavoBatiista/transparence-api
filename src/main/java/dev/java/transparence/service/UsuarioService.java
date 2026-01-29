package dev.java.transparence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.java.transparence.entity.Usuario;
import dev.java.transparence.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario incluirUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuario) {
        Usuario existente = buscarUsuarioPorId(id);
        existente.setNome(usuario.getNome());
        existente.setTelefone(usuario.getTelefone());
        existente.setEndereco(usuario.getEndereco());
        existente.setCidade(usuario.getCidade());
        existente.setEstado(usuario.getEstado());
        existente.setCep(usuario.getCep());
        return usuarioRepository.save(usuario);
    }

    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}