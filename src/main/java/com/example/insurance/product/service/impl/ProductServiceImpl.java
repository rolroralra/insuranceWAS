package com.example.insurance.product.service.impl;

import com.example.insurance.product.controller.dto.ProductDto;
import com.example.insurance.product.model.Product;
import com.example.insurance.product.repository.ProductRepository;
import com.example.insurance.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        Product product = new Product(productDto);

        return productRepository.save(product);
    }

    @Override
    public Product modifyProduct(ProductDto productDto) {
        Product product = productRepository.getById(productDto.getId());

        product = new Product(productDto);
        
        return productRepository.save(product);
    }

    @Override
    public Product removeProduct(Long productId) {
        Product product = productRepository.getById(productId);

//        Product removedProduct = product.clone();
        productRepository.deleteById(productId);
        return new Product(productId);
    }

    @Override
    public Boolean isExist(ProductDto productDto) {
        return isExist(productDto.getId());
    }

    @Override
    public Boolean isExist(Long productId) {
        return productRepository.findById(productId).isPresent();
    }
}
