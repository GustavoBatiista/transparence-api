package dev.java.transparence.service;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.java.transparence.dto.UsuarioRequestDTO;
import dev.java.transparence.dto.UsuarioResponseDTO;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO incluirUsuario(UsuarioRequestDTO dto) {
        log.info("Iniciando criação de usuário.");
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            log.warn("Tentativa de criar um usuário com CPF já existente.");
            throw new BusinessException("CPF já cadastrado");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Tentativa de criar um usuário com Email já existente.");
            throw new BusinessException("Email já cadastrado");
        }

        Usuario usuario = new Usuario(dto.getCpf(), dto.getNome(), dto.getEmail(), dto.getSenha(),
                dto.getTelefone(), dto.getEndereco(), dto.getCidade(), dto.getEstado(),
                dto.getCep());

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso. Id={}", salvo.getId());
        return toResponseDTO(salvo);
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO dto) {
        log.info("Iniciando atualização de usuário. Id={}", id);
        Usuario existente = buscarUsuarioEntityPorId(id);
        if (usuarioRepository.existsByEmail(dto.getEmail()) && !existente.getEmail().equals(dto.getEmail())) {
            log.warn("Tentativa de atualizar um usuário com Email já existente.");
            throw new BusinessException("Email já cadastrado");
        }
        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setTelefone(dto.getTelefone());
        existente.setEndereco(dto.getEndereco());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setCep(dto.getCep());
        log.info("Usuário atualizado com sucesso. Id={}", id);
        return toResponseDTO(usuarioRepository.save(existente));
    }

    public void excluirUsuario(Long id) {
        log.info("Iniciando exclusão de usuário. Id={}", id);
        if (!usuarioRepository.existsById(id)) {
            log.warn("Tentativa de excluir um usuário não existente. Id={}", id);
            throw new NotFoundException("Usuário não encontrado");
        }
        log.info("Usuário excluído com sucesso. Id={}", id);
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarUsuarioEntityPorId(Long id) {
        log.debug("Buscando usuário da base de dados por Id={}", id);
        return usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        log.info("Buscando usuário por Id={}", id);
        return toResponseDTO(buscarUsuarioEntityPorId(id));

    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        log.debug("Convertendo usuário para DTO. Id={}", usuario.getId());
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