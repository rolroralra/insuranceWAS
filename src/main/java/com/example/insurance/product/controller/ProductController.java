package com.example.insurance.product.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.product.controller.dto.ProductDto;
import com.example.insurance.product.model.Product;
import com.example.insurance.product.service.ProductService;
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
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/product", method = {RequestMethod.GET})
    ApiResult<List<Product>> getAllProducts() {
        return ApiResult.succeed(productService.getAllProducts());
    }

    @RequestMapping(value = "/product", method = {RequestMethod.POST})
    ApiResult<Product> addProduct(@RequestBody ProductDto productDto) {
        if (productService.isExist(productDto)) {
            return ApiResult.succeed(productService.modifyProduct(productDto));
        }

        return ApiResult.succeed(productService.addProduct(productDto));
    }

    @RequestMapping(value ="/product/{productId}", method = {RequestMethod.PUT})
    ApiResult<Product> modifyProduct(@PathVariable(value = "productId") Long productId, @RequestBody ProductDto productDto) {
        productDto.setId(productId);

        if (!productService.isExist(productId)) {
            return ApiResult.succeed(productService.addProduct(productDto));
        }

        return ApiResult.succeed(productService.modifyProduct(productDto));
    }

    @RequestMapping(value = "/product/{productId}", method = {RequestMethod.GET})
    ApiResult<Product> getProduct(@PathVariable(value = "productId") Long productId) {
        Product searchedProduct = productService.getProduct(productId);
        if (Objects.isNull(searchedProduct)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }

        return ApiResult.succeed(searchedProduct);
    }

    @RequestMapping(value ="/product/{productId}", method = {RequestMethod.DELETE})
    ApiResult<Product> removeProduct(@PathVariable(value = "productId") Long productId) {
        return ApiResult.succeed(productService.removeProduct(productId));
    }
}
