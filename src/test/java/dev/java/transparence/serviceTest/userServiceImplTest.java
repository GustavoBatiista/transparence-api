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

import dev.java.transparence.dto.UserRequestDTO;
import dev.java.transparence.dto.UserResponseDTO;
import dev.java.transparence.entity.User;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.UserRepository;
import dev.java.transparence.service.userServiceImpl;

@ExtendWith(MockitoExtension.class)
public class userServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private userServiceImpl userServiceImpl;

    private UserRequestDTO createUserRequestDTO() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setCpf("12345678901");
        dto.setEmail("teste@teste.com");
        dto.setname("Teste");
        dto.setpassword("123456");
        dto.setphone("12345678901");
        dto.setadress("Rua Teste, 123");
        dto.setcity("city Teste");
        dto.setstate("SP");
        dto.setzipCode("12345678");
        return dto;
    }

    private User createUserEntity() {
        return new User(
                "12345678901",
                "name Antigo",
                "emailantigo@teste.com",
                "123456",
                "12345678901",
                "Rua Antiga, 123",
                "city Antiga",
                "SP",
                "12345678");
    }

    @Test
    public void shouldCreateUserSuccessfully() {

        UserRequestDTO dto = createUserRequestDTO();

        when(userRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any()))
                .thenReturn(new User(dto.getCpf(), dto.getname(), dto.getEmail(), dto.getpassword(), dto.getphone(),
                        dto.getadress(), dto.getcity(), dto.getstate(), dto.getzipCode()));

        UserResponseDTO response = userServiceImpl.createUser(dto);
        assertEquals(dto.getCpf(), response.getCpf());
        assertEquals(dto.getname(), response.getname());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getphone(), response.getphone());
        assertEquals(dto.getadress(), response.getadress());
        assertEquals(dto.getcity(), response.getcity());
        assertEquals(dto.getstate(), response.getstate());
        assertEquals(dto.getzipCode(), response.getzipCode());

        verify(userRepository, times(1)).existsByCpf(dto.getCpf());
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void shouldThrowExceptionWhenCpfAlreadyExists() {

        UserRequestDTO dto = createUserRequestDTO();

        when(userRepository.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> userServiceImpl.createUser(dto));

        verify(userRepository, times(1)).existsByCpf(dto.getCpf());

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() {

        UserRequestDTO dto = createUserRequestDTO();

        when(userRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> userServiceImpl.createUser(dto));

        verify(userRepository).existsByCpf(dto.getCpf());
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldUpdateUserSuccessfully() {
        Long id = 1L;

        UserRequestDTO dto = createUserRequestDTO();
        
        dto.setname("name Atualizado");
        dto.setEmail("newemail@teste.com");
        dto.setphone("98765432100");
        dto.setadress("new Rua, 456");
        dto.setcity("new city");
        dto.setstate("RJ");
        dto.setzipCode("87654321");

        User user = createUserEntity();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));


        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);


        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userServiceImpl.updateUser(id, dto);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(dto.getname(), response.getname());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getphone(), response.getphone());
        assertEquals(dto.getadress(), response.getadress());
        assertEquals(dto.getcity(), response.getcity());
        assertEquals(dto.getstate(), response.getstate());
        assertEquals(dto.getzipCode(), response.getzipCode());
        assertEquals(user.getCpf(), response.getCpf()); // CPF não muda

        verify(userRepository).findById(id);
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldUpdateUserWithoutChangingEmail() {
        Long id = 1L;

        UserRequestDTO dto = createUserRequestDTO();

        User user = createUserEntity();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userServiceImpl.updateUser(id, dto);
        assertNotNull(response);
        assertEquals(dto.getname(), response.getname());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getphone(), response.getphone());
        assertEquals(dto.getadress(), response.getadress());
        assertEquals(dto.getcity(), response.getcity());
        assertEquals(dto.getstate(), response.getstate());
        assertEquals(dto.getzipCode(), response.getzipCode());
        assertEquals(user.getCpf(), response.getCpf());

        verify(userRepository).findById(id);
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistingUser() {

        Long id = 1L;
        UserRequestDTO dto = createUserRequestDTO();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.updateUser(id, dto));

        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingUserWithEmailAlreadyUsedByAnotherUser() {
        Long id = 1L;

        UserRequestDTO dto = createUserRequestDTO();
        dto.setname("name Atualizado");
        dto.setEmail("newemail@teste.com");
        dto.setphone("98765432100");
        dto.setadress("new Rua, 456");
        dto.setcity("new city");
        dto.setstate("RJ");
        dto.setzipCode("87654321");

        User userExistente = createUserEntity();

        when(userRepository.findById(id)).thenReturn(Optional.of(userExistente));

       
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> userServiceImpl.updateUser(id, dto));

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldDeleteUserSuccessfully() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);
        userServiceImpl.deleteUser(id);
        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistingUser() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.deleteUser(id));
        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    public void shouldReturnUserWhenFindingById() {

        Long id = 1L;
 
        User user = createUserEntity();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDTO response = userServiceImpl.findUserById(id);

        assertEquals(user.getCpf(), response.getCpf());

        verify(userRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionWhenFindingNonExistingUserById() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findUserById(id));
        verify(userRepository, times(1)).findById(id);
    }
}
