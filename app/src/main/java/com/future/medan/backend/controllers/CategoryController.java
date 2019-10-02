package com.future.medan.backend.controllers;

import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.responses.Response;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.responses.WebResponseConstructor;
import com.future.medan.backend.services.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public Response getAll(){
        return ResponseHelper.ok(categoryService.getAll().
                stream()
                .map(category -> WebResponseConstructor.toWebResponse(category))
                .collect(Collectors.toList())
        ) ;
    }

    @PostMapping(value = ApiPath.CATEGORIES, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category save(Category category) throws Exception {
        return categoryService.save(category);
    }
}
