package dev.java.transparence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.UserRequestDTO;
import dev.java.transparence.dto.UserResponseDTO;
import dev.java.transparence.entity.User;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {

        log.debug("Starting user creation.");

        if (userRepository.existsByCpf(dto.getCpf())) {
            log.warn("Attempt to create a user with an existing CPF.");
            throw new BusinessException("CPF already registered");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Attempt to create a user with an existing email. email={}", dto.getEmail());
            throw new BusinessException("Email already registered");
        }

        User user = new User(dto.getCpf(), dto.getName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()),
                dto.getPhone(), dto.getAddress(), dto.getCity(), dto.getState(),
                dto.getZipCode());

        User save = userRepository.save(user);

        log.info("User created successfully. userId={}", save.getId());

        return toResponseDTO(save);
    }

    // TODO: separar DTO de criação e atualização futuramente 09/02/2026
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        log.info("Starting user update. userId={}", id);

        User existente = findUserByContract(id);

        if (userRepository.existsByEmail(dto.getEmail()) && !existente.getEmail().equals(dto.getEmail())) {
            log.warn("Attempt to update a user with an existing email. email={}", dto.getEmail());
            throw new BusinessException("Email already registered");
        }

        existente.setName(dto.getName());
        existente.setEmail(dto.getEmail());
        existente.setPhone(dto.getPhone());
        existente.setAdress(dto.getAddress());
        existente.setCity(dto.getCity());
        existente.setState(dto.getState());
        existente.setZipCode(dto.getZipCode());

        log.info("User updated successfully. userId={}", id);

        return toResponseDTO(userRepository.save(existente));
    }

    @Override
    public void deleteUser(Long id) {

        log.info("Starting user deletion. userId={}", id);

        if (!userRepository.existsById(id)) {
            log.warn("Attempt to delete a non-existing user. userId={}", id);
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(id);

        log.info("User deleted successfully. userId={}", id);
    }

    @Override
    public User findUserByContract(Long id) {

        log.debug("Finding user in database. userId={}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserResponseDTO findUserById(Long id) {

        log.info("Finding user by id. userId={}", id);

        return toResponseDTO(findUserByContract(id));
    }

    private UserResponseDTO toResponseDTO(User user) {

        log.debug("Converting user to DTO. userId={}", user.getId());

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setCpf(user.getCpf());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAdress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setZipCode(user.getZipCode());

        return dto;
    }
}