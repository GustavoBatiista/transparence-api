package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import dev.java.transparence.dto.DependentRequestDTO;
import dev.java.transparence.dto.DependentResponseDTO;
import dev.java.transparence.entity.Dependent;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.DependentRepository;
import dev.java.transparence.service.dependentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class dependentServiceImplTest {

    @Mock
    private DependentRepository dependentRepository;

    @InjectMocks
    private dependentServiceImpl dependentServiceImpl;

    private DependentRequestDTO createDependentRequestDTO() {
        DependentRequestDTO dto = new DependentRequestDTO();
        dto.setCpf("12345678901");
        dto.setname("Teste");
        dto.setphone("12345678901");
        dto.setadress("Rua Teste, 123");
        dto.setcity("city Teste");
        dto.setstate("SP");
        dto.setzipCode("12345678");
        return dto;
    }

    private Dependent createDependentEntity() {
        return new Dependent("12345678901", "Teste", "12345678901", "Rua Teste, 123", "city Teste", "SP",
                "12345678");
    }

    @Test
    public void shouldCreateDependentSuccessfully() {
        DependentRequestDTO dto = createDependentRequestDTO();
        Dependent dependent = createDependentEntity();

        when(dependentRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(dependentRepository.save(any(Dependent.class))).thenReturn(dependent);

        DependentResponseDTO response = dependentServiceImpl.createDependent(dto);
        assertEquals(dto.getname(), response.getname());

        verify(dependentRepository).existsByCpf(dto.getCpf());
        verify(dependentRepository).save(any(Dependent.class));

    }

    @Test
    public void shouldThrowExceptionWhenCreatingDependentWithCpfAlreadyRegistered() {
        DependentRequestDTO dto = createDependentRequestDTO();

        when(dependentRepository.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThrows(BusinessException.class, () -> dependentServiceImpl.createDependent(dto));

        verify(dependentRepository).existsByCpf(dto.getCpf());
        verify(dependentRepository, never()).save(any(Dependent.class));

    }

    @Test
    public void shouldUpdateDependentSuccessfully() {
        Long id = 1L;
        DependentRequestDTO dto = createDependentRequestDTO();
        Dependent dependent = createDependentEntity();
        when(dependentRepository.findById(id)).thenReturn(Optional.of(dependent));
        when(dependentRepository.save(any(Dependent.class))).thenReturn(dependent);
        DependentResponseDTO response = dependentServiceImpl.updateDependent(id, dto);

        assertEquals(dto.getname(), response.getname());

        verify(dependentRepository).findById(id);
        verify(dependentRepository).save(any(Dependent.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistingDependent() {
        Long id = 1L;
        DependentRequestDTO dto = createDependentRequestDTO();

        when(dependentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dependentServiceImpl.updateDependent(id, dto));

        verify(dependentRepository).findById(id);
        verify(dependentRepository, never()).save(any(Dependent.class));
    }

    @Test
    public void shouldDeleteDependentSuccessfully() {
        Long id = 1L;
        when(dependentRepository.existsById(id)).thenReturn(true);
        dependentServiceImpl.deleteDependent(id);
        verify(dependentRepository, times(1)).existsById(id);
        verify(dependentRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistingDependent() {
        Long id = 1L;
        when(dependentRepository.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> dependentServiceImpl.deleteDependent(id));

        verify(dependentRepository).existsById(id);
        verify(dependentRepository, never()).deleteById(id);
    }

    @Test
    public void shouldFindDependentByIdSuccessfully() {
        Long id = 1L;
        Dependent dependent = createDependentEntity();
        when(dependentRepository.findById(id)).thenReturn(Optional.of(dependent));
        DependentResponseDTO response = dependentServiceImpl.findDependentById(id);

        assertEquals(dependent.getCpf(), response.getCpf());

        verify(dependentRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionWhenFindingNonExistingDependentById() {
        Long id = 1L;
        when(dependentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> dependentServiceImpl.findDependentById(id));
        verify(dependentRepository).findById(id);
    }

}
