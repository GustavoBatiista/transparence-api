package dev.java.transparence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.java.transparence.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
