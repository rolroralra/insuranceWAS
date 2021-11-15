package com.example.insurance.contract.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.contract.controller.dto.ContractDto;
import com.example.insurance.contract.model.Contract;
import com.example.insurance.contract.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/insurance", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ContractController {
    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @RequestMapping(value = "/contract", method = {RequestMethod.GET})
    ApiResult<List<Contract>> getAllContracts() {
        return ApiResult.succeed(contractService.getAllContracts());
    }

    @RequestMapping(value = "/contract", method = {RequestMethod.POST})
    ApiResult<Contract> addContract(@RequestBody ContractDto contractDto) {
        if (contractService.isExist(contractDto)) {
            return ApiResult.succeed(contractService.modifyContract(contractDto));
        }

        return ApiResult.succeed(contractService.addContract(contractDto));
    }

    @RequestMapping(value ="/contract/{contractId}", method = {RequestMethod.PUT})
    ApiResult<Contract> modifyContract(@PathVariable(value = "contractId") Long contractId, @RequestBody ContractDto contractDto) {
        contractDto.setId(contractId);

        if (!contractService.isExist(contractId)) {
            return ApiResult.succeed(contractService.addContract(contractDto));
        }

        return ApiResult.succeed(contractService.modifyContract(contractDto));
    }

    @RequestMapping(value = "/contract/{contractId}", method = {RequestMethod.GET})
    ApiResult<Contract> getContract(@PathVariable(value = "contractId") Long contractId) {
        Contract searchedContract = contractService.getContract(contractId);
        if (Objects.isNull(searchedContract)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract Not Found");
        }

        return ApiResult.succeed(searchedContract);
    }

    @RequestMapping(value ="/contract/{contractId}", method = {RequestMethod.DELETE})
    ApiResult<Contract> removeContract(@PathVariable(value = "contractId") Long contractId) {
        return ApiResult.succeed(contractService.removeContract(contractId));
    }
}
