package dev.java.transparence.service;

import dev.java.transparence.dto.UserRequestDTO;
import dev.java.transparence.dto.UserResponseDTO;
import dev.java.transparence.entity.User;

public interface UserService {

    public User findUserByContract(Long id);

    public UserResponseDTO createUser(UserRequestDTO dto);

    // TODO: separar DTO de criação e atualização futuramente 09/02/2026
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto);

    public void deleteUser(Long id);

    public UserResponseDTO findUserById(Long id);
}