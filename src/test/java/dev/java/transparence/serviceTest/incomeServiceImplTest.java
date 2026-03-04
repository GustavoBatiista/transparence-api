package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.java.transparence.dto.IncomeRequestDTO;
import dev.java.transparence.dto.IncomeResponseDTO;
import dev.java.transparence.entity.Contract;
import dev.java.transparence.entity.Dependent;
import dev.java.transparence.entity.Income;
import dev.java.transparence.entity.User;
import dev.java.transparence.enums.ContractStatus;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.IncomeRepository;
import dev.java.transparence.service.ContractService;
import dev.java.transparence.service.IncomeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class IncomeServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;
    @Mock
    private ContractService contractService;
    @InjectMocks
    private IncomeServiceImpl incomeServiceImpl;

    private IncomeRequestDTO createincomeRequestDTO() {
        IncomeRequestDTO dto = new IncomeRequestDTO();
        dto.setcontractId(1L);
        dto.setDescription("Teste");
        dto.setValue(BigDecimal.valueOf(100));
        dto.setDataIncome(LocalDate.now());
        return dto;
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

    private Contract createActiveContract() {

        Contract contract = new Contract(
                createUser(),
                createDependent(),
                LocalDate.now());

        ReflectionTestUtils.setField(contract, "id", 1L);
        ReflectionTestUtils.setField(contract, "status", ContractStatus.ACTIVE);

        return contract;
    }

    private Income createIncomeEntity() {
        Income income = new Income(
                createActiveContract(),
                "Teste",
                BigDecimal.valueOf(100),
                LocalDate.now());

        ReflectionTestUtils.setField(income, "id", 1L);

        return income;
    }

    @Test
    void shouldCreateIncomeSuccessfully() {

        IncomeRequestDTO dto = createincomeRequestDTO();
        Contract contract = createActiveContract();

        when(contractService.findContractForOperation(dto.getContractId()))
                .thenReturn(contract);

        when(incomeRepository.existsByContract_IdAndDataIncomeAndValue(
                dto.getContractId(), dto.getDataIncome(), dto.getValue())).thenReturn(false);

        when(incomeRepository.save(any())).thenReturn(createIncomeEntity());

        IncomeResponseDTO response = incomeServiceImpl.createIncome(dto);

        assertEquals(dto.getContractId(), response.getContractId());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getValue(), response.getValue());
        assertEquals(dto.getDataIncome(), response.getData());

        verify(contractService).findContractForOperation(dto.getContractId());
        verify(incomeRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCreatingIncomeAndContractIsNotActive() {
        IncomeRequestDTO dto = createincomeRequestDTO();
        Contract contract = createActiveContract();
        contract.setStatus(ContractStatus.CLOSED);

        when(contractService.findContractForOperation(dto.getContractId()))
                .thenReturn(contract);
        assertThrows(BusinessException.class, () -> incomeServiceImpl.createIncome(dto));

        verify(contractService).findContractForOperation(dto.getContractId());
        verify(incomeRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenIncomeAlreadyExists() {
        IncomeRequestDTO dto = createincomeRequestDTO();
        Contract contract = createActiveContract();

        when(contractService.findContractForOperation(dto.getContractId()))
                .thenReturn(contract);
        when(incomeRepository.existsByContract_IdAndDataIncomeAndValue(
                dto.getContractId(), dto.getDataIncome(), dto.getValue())).thenReturn(true);
        assertThrows(BusinessException.class, () -> incomeServiceImpl.createIncome(dto));

        verify(contractService).findContractForOperation(dto.getContractId());
        verify(incomeRepository, never()).save(any());
    }

    @Test
    void shouldUpdateIncomeSuccessfully() {
        IncomeRequestDTO dto = createincomeRequestDTO();
        Income income = createIncomeEntity();

        when(incomeRepository.findById(income.getId())).thenReturn(Optional.of(income));
        when(incomeRepository.save(any())).thenReturn(income);

        IncomeResponseDTO response = incomeServiceImpl.updateIncome(income.getId(), dto);

        assertEquals(income.getContract().getId(), response.getContractId());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getValue(), response.getValue());
        assertEquals(dto.getDataIncome(), response.getData());

        verify(incomeRepository).findById(income.getId());
        verify(incomeRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingIncomeAndContractIsNotActive() {
        IncomeRequestDTO dto = createincomeRequestDTO();
        Income income = createIncomeEntity();
        income.getContract().setStatus(ContractStatus.CLOSED);

        when(incomeRepository.findById(income.getId())).thenReturn(Optional.of(income));

        assertThrows(BusinessException.class,
                () -> incomeServiceImpl.updateIncome(income.getId(), dto));

        verify(incomeRepository).findById(income.getId());
        verify(incomeRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingIncome() {
        IncomeRequestDTO dto = createincomeRequestDTO();

        when(incomeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> incomeServiceImpl.updateIncome(1L, dto));

        verify(incomeRepository).findById(1L);
        verify(incomeRepository, never()).save(any());
    }

    @Test
    void shouldDeleteIncomeSuccessfully() {
        Income income = createIncomeEntity();
        when(incomeRepository.findById(income.getId())).thenReturn(Optional.of(income));
        incomeServiceImpl.deleteIncome(income.getId());
        verify(incomeRepository).findById(income.getId());
        verify(incomeRepository).deleteById(income.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingIncomeAndContractIsNotActive() {
        Income income = createIncomeEntity();
        income.getContract().setStatus(ContractStatus.CLOSED);
        when(incomeRepository.findById(income.getId())).thenReturn(Optional.of(income));
        assertThrows(BusinessException.class, () -> incomeServiceImpl.deleteIncome(income.getId()));
        verify(incomeRepository).findById(income.getId());
        verify(incomeRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingIncome() {
        when(incomeRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> incomeServiceImpl.deleteIncome(1L));
        verify(incomeRepository).findById(1L);
        verify(incomeRepository, never()).deleteById(any());
    }

    @Test
    void shouldReturnIncomeList() {
        List<Income> incomes = new ArrayList<>();
        when(incomeRepository.findAll()).thenReturn(incomes);
        List<IncomeResponseDTO> response = incomeServiceImpl.findAllIncome();
        assertEquals(incomes.size(), response.size());
        verify(incomeRepository).findAll();
    }

    @Test
    void shouldReturnIncomeWhenFindingById() {
        Income income = createIncomeEntity();
        when(incomeRepository.findById(income.getId())).thenReturn(Optional.of(income));
        IncomeResponseDTO response = incomeServiceImpl.findIncomeById(income.getId());
        assertEquals(income.getId(), response.getId());
        verify(incomeRepository).findById(income.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingIncomeById() {
        when(incomeRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> incomeServiceImpl.findIncomeById(1L));
        verify(incomeRepository).findById(1L);
    }
}
