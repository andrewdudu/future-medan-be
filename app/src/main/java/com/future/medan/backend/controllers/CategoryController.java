package com.future.medan.backend.controllers;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.constants.ApiPath;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Api
@RestController
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping(ApiPath.CATEGORIES)
    public Response<List<CategoryWebResponse>> getAll(){
        return ResponseHelper.ok(categoryService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        ) ;
    }

    @GetMapping(ApiPath.CATEGORY_BY_CATEGORY_ID)
    public Response<CategoryWebResponse> getById(@PathVariable String id) {
        Optional<Category> category = categoryService.getById(id);

        if (!category.isPresent())
            throw new ResourceNotFoundException("Category", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(category.get()));
    }

    @PostMapping(value = ApiPath.CATEGORIES, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CategoryWebResponse> save(@RequestBody Category category) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.save(category)));
    }

    @PutMapping(value = ApiPath.CATEGORY_BY_CATEGORY_ID, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CategoryWebResponse> editById(@RequestBody Category category, @PathVariable String id){
        Optional<Category> findCategory = categoryService.getById(id);

        if (!findCategory.isPresent())
            throw new ResourceNotFoundException("Category", "id", id);

        category.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.save(category)));
    }

    @DeleteMapping(value = ApiPath.CATEGORY_BY_CATEGORY_ID)
    public void deleteById(String id){
        Optional<Category> category = categoryService.getById(id);

        if (!category.isPresent())
            throw new ResourceNotFoundException("Category", "id", id);

        categoryService.deleteById(id);
    }
}
