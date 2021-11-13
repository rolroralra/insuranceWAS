package com.example.insurance.repository;

import com.example.insurance.contract.model.Contract;
import com.example.insurance.contract.repository.ContractRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContractRepositoryTests {
    @Autowired
    private ContractRepository contractRepository;

    @BeforeEach
    void setUp() {
        contractRepository.save(new Contract());
    }

    @Test
    void test_find_all() {
        Assertions.assertThat(contractRepository.findAll()).isNotEmpty();
    }

    @Test
    void test_find_by_id() {
        contractRepository.findAll().forEach(contract ->
                Assertions.assertThat(contractRepository.findById(contract.getId())).isPresent()
        );
    }

    @Test
    void test_save() {
        int originalSize = contractRepository.findAll().size();

        Contract contract = contractRepository.save(new Contract());

        Assertions.assertThat(contractRepository.findById(contract.getId()))
                .isPresent();

        Assertions.assertThat(originalSize + 1 == contractRepository.findAll().size()).isTrue();
    }

}
