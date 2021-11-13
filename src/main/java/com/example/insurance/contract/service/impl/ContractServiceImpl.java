package com.example.insurance.contract.service.impl;

import com.example.insurance.contract.controller.dto.ContractDto;
import com.example.insurance.contract.model.Contract;
import com.example.insurance.contract.repository.ContractRepository;
import com.example.insurance.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract getContract(Long contractId) {
        return contractRepository.findById(contractId).orElse(null);
    }

    @Override
    public Contract addContract(ContractDto contractDto) {
        Contract contract = new Contract(contractDto);

        return contractRepository.save(contract);
    }

    @Override
    public Contract modifyContract(ContractDto contractDto) {
        Contract contract = contractRepository.getById(contractDto.getId());

        contract = new Contract(contractDto);
        
        return contractRepository.save(contract);
    }

    @Override
    public Contract removeContract(Long contractId) {
        Contract contract = contractRepository.getById(contractId);

//        Contract removedContract = contract.clone();
        contractRepository.deleteById(contractId);
        return new Contract(contractId);
    }

    @Override
    public Boolean isExist(ContractDto contractDto) {
        return isExist(contractDto.getId());
    }

    @Override
    public Boolean isExist(Long contractId) {
        return contractRepository.findById(contractId).isPresent();
    }
}
