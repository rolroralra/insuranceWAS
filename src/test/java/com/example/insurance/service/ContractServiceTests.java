package com.example.insurance.service;

import com.example.insurance.contract.controller.dto.ContractDto;
import com.example.insurance.contract.model.Contract;
import com.example.insurance.contract.service.ContractService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ContractServiceTests {
    @Autowired
    private ContractService contractService;

    @BeforeEach
    void setUp() {
        contractService.addContract(new ContractDto());
    }

    @Test
    void test_add_contract() {
        Contract contract = contractService.addContract(new ContractDto());

        Assertions.assertThat(contract).isNotNull();
    }

    @Test
    void test_get_all_contracts() {
        Assertions.assertThat(contractService.getAllContracts()).isNotEmpty();
    }

    @Test
    void test_get_contract_by_id() {
        Optional<Contract> anyContract = contractService.getAllContracts().stream().findFirst();

        Assertions.assertThat(anyContract).isPresent();
        Assertions.assertThat(contractService.getContract(anyContract.get().getId())).isNotNull();
    }

    @Test
    void test_modify_contract() {
        Optional<Contract> anyContract = contractService.getAllContracts().stream().findFirst();

        Assertions.assertThat(anyContract).isPresent();

        ContractDto contractDto = new ContractDto(anyContract.get());
        contractDto.setName("TEST");

        Contract modifiedContract = contractService.modifyContract(contractDto);

        Assertions.assertThat(new ContractDto(modifiedContract)).isEqualTo(contractDto);

        contractDto.setName("CHANGED");
        modifiedContract = contractService.modifyContract(contractDto);

        Assertions.assertThat(new ContractDto(modifiedContract)).isEqualTo(contractDto);
        Assertions.assertThat(contractService.getContract(contractDto.getId())).isEqualTo(modifiedContract);
    }

    @Test
    void test_remove_contract() {
        Optional<Contract> anyContract = contractService.getAllContracts().stream().findFirst();

        int originalSize = contractService.getAllContracts().size();

        Assertions.assertThat(anyContract).isPresent();
        contractService.removeContract(anyContract.get().getId());

        Assertions.assertThat(contractService.getContract(anyContract.get().getId())).isNull();

        Assertions.assertThat(originalSize - 1 == contractService.getAllContracts().size()).isTrue();
    }

    @Test
    void test_is_exist_by_id() {
        Optional<Contract> anyContract = contractService.getAllContracts().stream().findFirst();

        if (!anyContract.isPresent()) {
            Assertions.fail("There is no row record.");
            return;
        }

        Assertions.assertThat(contractService.isExist(anyContract.get().getId())).isNotNull();
    }

    @Test
    void test_is_exist() {
        Optional<Contract> anyContract = contractService.getAllContracts().stream().findFirst();

        if (!anyContract.isPresent()) {
            Assertions.fail("There is no row record.");
            return;
        }

        Assertions.assertThat(contractService.isExist(new ContractDto(anyContract.get()))).isNotNull();
    }
}
