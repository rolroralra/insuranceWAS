package com.example.insurance.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.product.controller.dto.ProductDto;
import com.example.insurance.product.model.Product;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {
    private final MockMvc mockMvc;

    @Autowired
    public ProductControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void test_get_all_products() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/product")
                                .accept(MediaType.APPLICATION_JSON)
                                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<List<Product>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Product.class).getType()).getType());

        System.out.println(apiResult);
    }

    @Test
    void test_get_product_by_id() throws  Exception {
        Product product = getAnyProduct();

        Assertions.assertThat(product)
                .isNotNull()
                .isEqualTo(getProductById(product.getId()));
    }

    @Test
    void test_get_product_by_id_not_found() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/product/{productId}", 7777L)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void test_add_product() throws Exception {
        createAndSaveAnyProduct();
    }

    @Test
    void test_add_product_with_already_exist_product() throws Exception {
        Product product = getAnyProduct();

        product.setName("Added But Modified Product");
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/product")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(product.toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Product> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Product.class).getType());

        Product addedButModifiedProduct = apiResult.getData();

        Assertions.assertThat(addedButModifiedProduct)
                .isNotNull()
                .isEqualTo(getProductById(addedButModifiedProduct.getId()));
    }

    @Test
    void test_modify_product() throws Exception {
        Product product = getAnyProduct();

        product.setName("CHANGED Product");

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/product/{productId}", product.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ProductDto(product).toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Product> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Product.class).getType());

        Product modifiedProduct = apiResult.getData();

        Assertions.assertThat(modifiedProduct)
                .isNotNull()
                .isEqualTo(product)
                .isEqualTo(getProductById(modifiedProduct.getId()));

    }

    @Test
    void test_modify_product_with_not_exists_product() throws Exception {
        Product product = new Product();
        product.setName("NEW CONTRACT");
        product.setId(7777L);

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/product/{productId}", product.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ProductDto(product).toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Product> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Product.class).getType());

        Product modifiedButAddedProduct = apiResult.getData();

        Assertions.assertThat(modifiedButAddedProduct)
                .isNotNull();

    }

    @Test
    void test_remove_product() throws Exception {
        Product product = createAndSaveAnyProduct();

        System.out.println(product);
        ResultActions result =
                mockMvc.perform(
                        delete("/api/v1/insurance/product/{productId}", product.getId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Product> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Product.class).getType());

        Product deletedProduct = apiResult.getData();

        result =
                mockMvc.perform(
                        get("/api/v1/insurance/product/{productId}", deletedProduct.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isNotFound());
    }

    private Product getAnyProduct() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/product")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<List<Product>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Product.class).getType()).getType());

        return apiResult.getData().stream().findAny().orElseThrow(() -> new Exception("There is no data."));
    }

    private Product getProductById(Long productId) throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/product/{productId}", productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Product> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class,  Product.class).getType());

        Assertions.assertThat(apiResult.getData())
                .isNotNull();

        return apiResult.getData();
    }

    private Product createAndSaveAnyProduct() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/product")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ProductDto(1L, "Product01").toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Product> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Product.class).getType());

        Product addedProduct = apiResult.getData();

        Assertions.assertThat(addedProduct)
                .isNotNull()
                .isEqualTo(getProductById(addedProduct.getId()));

        return addedProduct;
    }
}
