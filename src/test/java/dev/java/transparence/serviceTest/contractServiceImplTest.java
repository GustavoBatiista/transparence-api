package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.java.transparence.dto.ContractRequestDTO;
import dev.java.transparence.dto.ContractResponseDTO;
import dev.java.transparence.entity.Contract;
import dev.java.transparence.entity.Dependent;
import dev.java.transparence.entity.User;
import dev.java.transparence.enums.ContractStatus;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.ContractRepository;
import dev.java.transparence.service.ContractServiceImpl;
import dev.java.transparence.service.dependentService;
import dev.java.transparence.service.userService;

@ExtendWith(MockitoExtension.class)
public class contractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private userService userService;

    @Mock
    private dependentService dependentService;

    @InjectMocks
    private ContractServiceImpl contractServiceImpl;

    private ContractRequestDTO createContractRequestDTO() {
        ContractRequestDTO dto = new ContractRequestDTO();
        dto.setUserId(1L);
        dto.setDependentId(1L);
        dto.setStartDate(LocalDate.now());
        return dto;
    }

    private Contract createContractEntity() {

        Contract contract = new Contract(
                createUser(),
                createDependent(),
                LocalDate.now());

        ReflectionTestUtils.setField(contract, "id", 1L);
        ReflectionTestUtils.setField(contract, "status", ContractStatus.ACTIVE);

        return contract;
    }

    private User createUser() {
        User user = new User(
                "12345678901", "Teste", "teste@teste.com", "123456",
                "11999999999", "Rua", "city", "SP", "12345678");

        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }

    private Dependent createDependent() {
        Dependent pessoa = new Dependent(
                "12345678901", "Teste", "11999999999",
                "Rua", "city", "SP", "12345678");

        ReflectionTestUtils.setField(pessoa, "id", 1L);

        return pessoa;
    }

    private void mockSaveComId() {
        when(contractRepository.save(any()))
                .thenAnswer(invocation -> {
                    Contract c = invocation.getArgument(0);
                    ReflectionTestUtils.setField(c, "id", 1L);
                    return c;
                });
    }

    @Test
    public void shouldCreateContractSuccessfully() {
        ContractRequestDTO dto = createContractRequestDTO();
        User user = createUser();
        Dependent dependent = createDependent();

        when(userService.findUserByContract(dto.getUserId()))
                .thenReturn(user);

        when(dependentService.findDependentEntityById(dto.getDependentId()))
                .thenReturn(dependent);

        when(contractRepository.existsByUser_IdAndDependent_IdAndStatus(user.getId(),
                dependent.getId(), ContractStatus.ACTIVE)).thenReturn(false);

        mockSaveComId();

        ContractResponseDTO response = contractServiceImpl.createContract(dto);

        assertEquals(1L, response.getId());
        assertEquals(dto.getUserId(), response.getUserId());
        assertEquals(dto.getDependentId(), response.getDependentId());
        assertEquals(dto.getStartDate(), response.getStartDate());
        assertEquals(ContractStatus.ACTIVE, response.getStatus());

        verify(userService).findUserByContract(dto.getUserId());
        verify(dependentService).findDependentEntityById(dto.getDependentId());
        verify(contractRepository).existsByUser_IdAndDependent_IdAndStatus(dto.getUserId(),
                dto.getDependentId(), ContractStatus.ACTIVE);
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    public void shouldThrowExceptionWhenContractAlreadyExists() {
        ContractRequestDTO dto = createContractRequestDTO();
        User user = createUser();
        Dependent dependent = createDependent();
        when(userService.findUserByContract(dto.getUserId())).thenReturn(user);
        when(dependentService.findDependentEntityById(dto.getDependentId())).thenReturn(dependent);

        when(contractRepository.existsByUser_IdAndDependent_IdAndStatus(user.getId(),
                dependent.getId(), ContractStatus.ACTIVE)).thenReturn(true);

        assertThrows(BusinessException.class, () -> contractServiceImpl.createContract(dto));

        verify(userService).findUserByContract(dto.getUserId());
        verify(dependentService).findDependentEntityById(dto.getDependentId());
        verify(contractRepository).existsByUser_IdAndDependent_IdAndStatus(user.getId(),
                dependent.getId(), ContractStatus.ACTIVE);
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void shouldCloseContractSuccessfully() {

        Contract contract = createContractEntity();

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));

        mockSaveComId();

        ContractResponseDTO response = contractServiceImpl.closeContract(contract.getId());

        assertEquals(contract.getId(), response.getId());
        assertEquals(ContractStatus.CLOSED, response.getStatus());
        assertEquals(LocalDate.now(), response.getEndDate());

        verify(contractRepository).findById(contract.getId());
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    public void shouldThrowExceptionWhenContractIsAlreadyClosed() {

        Contract contract = createContractEntity();
        contract.setStatus(ContractStatus.CLOSED);

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));

        assertThrows(BusinessException.class, () -> contractServiceImpl.closeContract(contract.getId()));

        verify(contractRepository).findById(contract.getId());
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void shouldSuspendContractSuccessfully() {

        Contract contract = createContractEntity();

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));
        mockSaveComId();

        ContractResponseDTO response = contractServiceImpl.suspendContract(contract.getId());

        assertEquals(contract.getId(), response.getId());
        assertEquals(ContractStatus.SUSPENDED, response.getStatus());

        verify(contractRepository).findById(contract.getId());
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    public void shouldThrowExceptionWhenContractIsAlreadySuspendedOrClosed() {

        Contract contract = createContractEntity();
        contract.setStatus(ContractStatus.SUSPENDED);

        when(contractRepository.findById(contract.getId()))
                .thenReturn(Optional.of(contract));

        assertThrows(BusinessException.class,
                () -> contractServiceImpl.suspendContract(contract.getId()));

        verify(contractRepository).findById(contract.getId());
        verify(contractRepository, never()).save(any());
    }

    @Test
    public void shouldReactivateContractSuccessfully() {

        Contract contract = createContractEntity();

        ReflectionTestUtils.setField(contract, "status", ContractStatus.SUSPENDED);

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));
        mockSaveComId();
        ContractResponseDTO response = contractServiceImpl.reactivateContract(contract.getId());

        assertEquals(contract.getId(), response.getId());
        assertEquals(ContractStatus.ACTIVE, response.getStatus());

        verify(contractRepository).findById(contract.getId());
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    public void shouldThrowExceptionWhenContractIsNotSuspended() {
        Contract contract = createContractEntity();

        when(contractRepository.findById(contract.getId()))
                .thenReturn(Optional.of(contract));

        assertThrows(BusinessException.class,
                () -> contractServiceImpl.reactivateContract(contract.getId()));

        verify(contractRepository).findById(contract.getId());
        verify(contractRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteContractSuccessfully() {
        Contract contract = createContractEntity();

        when(contractRepository.existsById(contract.getId())).thenReturn(true);

        contractServiceImpl.deleteContract(contract.getId());

        verify(contractRepository).existsById(contract.getId());
        verify(contractRepository).deleteById(contract.getId());
    }

    @Test
    public void shouldThrowExceptionWhenContractDoesNotExist() {
        Contract contract = createContractEntity();
        when(contractRepository.existsById(contract.getId())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> contractServiceImpl.deleteContract(contract.getId()));
        verify(contractRepository).existsById(contract.getId());
        verify(contractRepository, never()).deleteById(contract.getId());
    }

    @Test
    public void shouldFindContractByIdSuccessfully() {
        Contract contract = createContractEntity();
        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));
        ContractResponseDTO response = contractServiceImpl.findContractById(contract.getId());
        assertEquals(contract.getId(), response.getId());
        assertEquals(contract.getUser().getId(), response.getUserId());
        assertEquals(contract.getDependent().getId(), response.getDependentId());
        assertEquals(contract.getStartDate(), response.getStartDate());
        assertEquals(contract.getStatus(), response.getStatus());

        verify(contractRepository).findById(contract.getId());
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void shouldThrowExceptionWhenContractDoesNotExistWhenFindingById() {
        Contract contract = createContractEntity();
        when(contractRepository.findById(contract.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> contractServiceImpl.findContractById(contract.getId()));
        verify(contractRepository).findById(contract.getId());
        verify(contractRepository, never()).save(any(Contract.class));
    }
}
