package com.future.medan.backend.services.controllers;

import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.payload.responses.CategoryWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Response<List<CategoryWebResponse>> getAll(){
        return ResponseHelper.ok(categoryService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        ) ;
    }

    @GetMapping("/categories/{id}")
    public Response<CategoryWebResponse> getById(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.getById(id)));
    }

    @PostMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CategoryWebResponse> save(@RequestBody Category category) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.save(category)));
    }

    @PutMapping(value = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CategoryWebResponse> editById(@RequestBody Category category, @PathVariable String id){
        category.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.save(category, id)));
    }

    @DeleteMapping(value = "/categories/{id}")
    public void deleteById(@PathVariable String id){
        categoryService.deleteById(id);
    }
}
