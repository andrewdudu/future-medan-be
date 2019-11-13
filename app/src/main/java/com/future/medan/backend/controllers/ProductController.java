package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.payload.requests.ProductWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.ProductWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.CategoryService;
import com.future.medan.backend.services.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class ProductController {

    private ProductService productService;

    private CategoryService categoryService;

    @Autowired
    public ProductController(ProductService service, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.productService = service;
    }

    @GetMapping("/api/products")
    public Response<List<ProductWebResponse>> getAll() {
        return ResponseHelper.ok(productService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/products/{id}")
    public Response<ProductWebResponse> getById(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.getById(id)));
    }

    @PostMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> save(@Validated @RequestBody ProductWebRequest productWebRequest) throws IOException {
        Product product = WebRequestConstructor.toProductEntity(productWebRequest);
        Category category = categoryService.getById(productWebRequest.getCategory());
        product.setCategory(category);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product)));
    }

    @PutMapping(value = "/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> editById(@RequestBody Product product, @PathVariable String id) {
        product.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product, id)));
    }

    @DeleteMapping("/api/products/{id}")
    public void deleteById(@PathVariable String id){
        productService.deleteById(id);
    }
}
