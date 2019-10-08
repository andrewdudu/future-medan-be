package com.future.medan.backend.controllers;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.responses.ProductWebResponse;
import com.future.medan.backend.responses.Response;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.responses.WebResponseConstructor;
import com.future.medan.backend.services.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api
@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService service) {
        this.productService = service;
    }

    @GetMapping(ApiPath.PRODUCTS)
    public Response<List<ProductWebResponse>> getAll() {
        return ResponseHelper.ok(productService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping(ApiPath.PRODUCT_BY_PRODUCT_ID)
    public Response<ProductWebResponse> getById(@PathVariable String id) {
        Optional<Product> product = productService.getById(id);

        if (!product.isPresent())
            throw new ResourceNotFoundException("Product", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(product.get()));
    }

    @PostMapping(value = ApiPath.PRODUCTS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> save(@RequestBody Product product) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product)));
    }

    @PutMapping(value = ApiPath.PRODUCT_BY_PRODUCT_ID, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> editById(@RequestBody Product product, @PathVariable String id) {
        Optional<Product> findProduct = productService.getById(id);

        if (!findProduct.isPresent())
            throw new ResourceNotFoundException("Product", "id", id);

        product.setId(id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product)));
    }

    @DeleteMapping(ApiPath.PRODUCT_BY_PRODUCT_ID)
    public void deleteById(@PathVariable String id){
        Optional<Product> product = productService.getById(id);

        if (!product.isPresent())
            throw new ResourceNotFoundException("Product", "id", id);

        productService.deleteById(id);
    }
}
