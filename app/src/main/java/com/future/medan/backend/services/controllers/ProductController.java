package com.future.medan.backend.services.controllers;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.payload.responses.ProductWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService service) {
        this.productService = service;
    }

    @GetMapping("/products")
    public Response<List<ProductWebResponse>> getAll() {
        return ResponseHelper.ok(productService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/products/{id}")
    public Response<ProductWebResponse> getById(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.getById(id)));
    }

    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> save(@RequestBody Product product) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product)));
    }

    @PutMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> editById(@RequestBody Product product, @PathVariable String id) {
        product.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product, id)));
    }

    @DeleteMapping("/products")
    public void deleteById(@PathVariable String id){
        productService.deleteById(id);
    }
}
