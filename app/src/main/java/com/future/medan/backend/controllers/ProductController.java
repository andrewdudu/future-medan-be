package com.future.medan.backend.controllers;

import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.services.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService service) {
        this.productService = service;
    }

    @GetMapping(ApiPath.PRODUCTS)
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping(ApiPath.PRODUCT_BY_PRODUCT_ID)
    public Product getOne(@PathVariable String id){
        return productService.getOne(id);
    }

    @PostMapping(value = ApiPath.PRODUCTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product save(@RequestBody Product product) throws Exception {
        return productService.save(product);
    }

    @DeleteMapping(ApiPath.PRODUCT_BY_PRODUCT_ID)
    public void deleteById(@PathVariable String id) { productService.deleteById(id); }
}
