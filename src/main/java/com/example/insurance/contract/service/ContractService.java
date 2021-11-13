package com.example.insurance.contract.service;

import com.example.insurance.contract.controller.dto.ContractDto;
import com.example.insurance.contract.model.Contract;

import java.util.List;

public interface ContractService {
    List<Contract> getAllContracts();
    Contract getContract(Long contractId);
    Contract addContract(ContractDto contractDto);
    Contract modifyContract(ContractDto contractDto);
    Contract removeContract(Long contractId);

    Boolean isExist(ContractDto contractDto);
    Boolean isExist(Long contractId);
}
