package dev.java.transparence.service;

import dev.java.transparence.dto.ContractRequestDTO;
import dev.java.transparence.dto.ContractResponseDTO;
import dev.java.transparence.entity.Contract;


public interface ContractService {

    Contract findContractForOperation(Long contractId);

    public ContractResponseDTO createContract(ContractRequestDTO dto);

    public ContractResponseDTO closeContract(Long id);

    public ContractResponseDTO suspendContract(Long id);

    public ContractResponseDTO reactivateContract(Long id); 

    public void deleteContract(Long id); 

    public ContractResponseDTO findContractById(Long id);

}