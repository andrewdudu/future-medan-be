package com.future.medan.backend.controllers;

import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.responses.Response;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.services.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService service) {
        this.productService = service;
    }

    @GetMapping(ApiPath.PRODUCTS)
    public Response getAll() {
        return ResponseHelper.ok(productService.getAll());
    }

    @PostMapping(value = ApiPath.PRODUCTS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product save(@RequestBody Product product) throws Exception {
        return productService.save(product);
    }
}
