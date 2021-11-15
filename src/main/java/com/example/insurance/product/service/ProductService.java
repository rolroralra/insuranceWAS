package com.example.insurance.product.service;

import com.example.insurance.product.controller.dto.ProductDto;
import com.example.insurance.product.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProduct(Long productId);
    Product addProduct(ProductDto productDto);
    Product modifyProduct(ProductDto productDto);
    Product removeProduct(Long productId);

    Boolean isExist(ProductDto productDto);
    Boolean isExist(Long productId);
}
