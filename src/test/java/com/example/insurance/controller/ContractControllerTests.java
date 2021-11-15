package com.example.insurance.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.common.GsonUtil;
import com.example.insurance.contract.controller.dto.ContractDto;
import com.example.insurance.contract.model.Contract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContractControllerTests {
    private final MockMvc mockMvc;

    @Autowired
    public ContractControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private ResultMatcher jsonResultMatcher() {
        return result -> {
            if (!GsonUtil.isJsonValid(result.getResponse().getContentAsString())) {
                throw new Exception("Json Parser Failed.");
            }
        };
    }

    @Test
    void test_get_all_contracts() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/contract")
                                .accept(MediaType.APPLICATION_JSON)
                                );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonResultMatcher());

        ApiResult<List<Contract>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Contract.class).getType()).getType());

        System.out.println(apiResult);
    }

    @Test
    void test_get_contract_by_id() throws  Exception {
        Contract contract = getAnyContract();

        Assertions.assertThat(contract)
                .isNotNull()
                .isEqualTo(getContractById(contract.getId()));
    }

    @Test
    void test_get_contract_by_id_not_found() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/contract/{contractId}", 7777L)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void test_add_contract() throws Exception {
        createAndSaveAnyContract();
    }

    @Test
    void test_add_contract_with_already_exist_contract() throws Exception {
        Contract contract = getAnyContract();

        contract.setName("Added But Modified Contract");
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/contract")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(contract.toString())
                );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonResultMatcher());

        ApiResult<Contract> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Contract.class).getType());

        Contract addedButModifiedContract = apiResult.getData();

        Assertions.assertThat(addedButModifiedContract)
                .isNotNull()
                .isEqualTo(getContractById(addedButModifiedContract.getId()));
    }

    @Test
    void test_modify_contract() throws Exception {
        Contract contract = getAnyContract();

        contract.setName("CHANGED Contract");

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/contract/{contractId}", contract.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ContractDto(contract).toString())
                );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonResultMatcher());

        ApiResult<Contract> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Contract.class).getType());

        Contract modifiedContract = apiResult.getData();

        Assertions.assertThat(modifiedContract)
                .isNotNull()
                .isEqualTo(contract)
                .isEqualTo(getContractById(modifiedContract.getId()));

    }

    @Test
    void test_modify_contract_with_not_exists_contract() throws Exception {
        Contract contract = new Contract();
        contract.setName("NEW CONTRACT");
        contract.setId(7777L);

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/contract/{contractId}", contract.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ContractDto(contract).toString())
                );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonResultMatcher());

        ApiResult<Contract> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Contract.class).getType());

        Contract modifiedButAddedContract = apiResult.getData();

        Assertions.assertThat(modifiedButAddedContract)
                .isNotNull();

    }

    @Test
    void test_remove_contract() throws Exception {
        Contract contract = createAndSaveAnyContract();

        System.out.println(contract);
        ResultActions result =
                mockMvc.perform(
                        delete("/api/v1/insurance/contract/{contractId}", contract.getId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonResultMatcher());

        ApiResult<Contract> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Contract.class).getType());

        Contract deletedContract = apiResult.getData();

        result =
                mockMvc.perform(
                        get("/api/v1/insurance/contract/{contractId}", deletedContract.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isNotFound());
    }

    private Contract getAnyContract() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/contract")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonResultMatcher());

        ApiResult<List<Contract>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Contract.class).getType()).getType());

        return apiResult.getData().stream().findAny().orElseThrow(() -> new Exception("There is no data."));
    }

    private Contract getContractById(Long contractId) throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/contract/{contractId}", contractId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Contract> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class,  Contract.class).getType());

        Assertions.assertThat(apiResult.getData())
                .isNotNull();

        return apiResult.getData();
    }

    private Contract createAndSaveAnyContract() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/contract")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ContractDto(1L, "Contract01").toString())
                );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonResultMatcher());

        ApiResult<Contract> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Contract.class).getType());

        Contract addedContract = apiResult.getData();

        Assertions.assertThat(addedContract)
                .isNotNull()
                .isEqualTo(getContractById(addedContract.getId()));

        return addedContract;
    }
}
