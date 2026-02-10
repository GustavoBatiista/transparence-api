package dev.java.transparence.service;

import dev.java.transparence.dto.UsuarioRequestDTO;
import dev.java.transparence.dto.UsuarioResponseDTO;
import dev.java.transparence.entity.Usuario;

public interface UsuarioService {

    public Usuario buscarUsuarioParaContrato(Long id);

    public UsuarioResponseDTO incluirUsuario(UsuarioRequestDTO dto);

    // TODO: separar DTO de criação e atualização futuramente 09/02/2026
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO dto);

    public void excluirUsuario(Long id);

    public UsuarioResponseDTO buscarUsuarioPorId(Long id);
}