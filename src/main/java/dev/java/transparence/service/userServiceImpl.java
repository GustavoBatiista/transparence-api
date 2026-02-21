package dev.java.transparence.service;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.java.transparence.dto.UserRequestDTO;
import dev.java.transparence.dto.UserResponseDTO;
import dev.java.transparence.entity.User;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.UserRepository;

@Service
public class userServiceImpl implements userService {

    private static final Logger log = LoggerFactory.getLogger(userServiceImpl.class);

    private UserRepository userRepository;

    public userServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        User user = new User(dto.getCpf(), dto.getname(), dto.getEmail(), dto.getpassword(),
                dto.getphone(), dto.getadress(), dto.getcity(), dto.getstate(),
                dto.getzipCode());

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

        existente.setname(dto.getname());
        existente.setEmail(dto.getEmail());
        existente.setphone(dto.getphone());
        existente.setadress(dto.getadress());
        existente.setcity(dto.getcity());
        existente.setstate(dto.getstate());
        existente.setzipCode(dto.getzipCode());

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
        dto.setname(user.getname());
        dto.setEmail(user.getEmail());
        dto.setphone(user.getphone());
        dto.setadress(user.getadress());
        dto.setcity(user.getcity());
        dto.setstate(user.getstate());
        dto.setzipCode(user.getzipCode());

        return dto;
    }
}