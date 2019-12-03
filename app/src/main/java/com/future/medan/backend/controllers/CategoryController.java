package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.payload.requests.CategoryWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.services.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/api/categories")
    public Response<List<CategoryWebResponse>> getAll(){
        return ResponseHelper.ok(categoryService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        ) ;
    }

    @GetMapping(value = "/api/categories/paginate", params = {"page", "size"})
    public PaginationResponse<List<CategoryWebResponse>> findPaginated(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        Page<Category> resultPage = categoryService.findPaginated(page, size);

        return ResponseHelper.ok(resultPage.getContent()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), resultPage.getTotalElements(), resultPage.getTotalPages());
    }

    @GetMapping("/api/categories/{id}")
    public Response<CategoryWebResponse> getById(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.getById(id)));
    }

    @PostMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CategoryWebResponse> save(@RequestBody CategoryWebRequest categoryWebRequest) throws IOException {
        Category category = WebRequestConstructor.toCategoryEntity(categoryWebRequest);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.save(category)));
    }

    @PostMapping(value = "/api/categories/hide/{id}")
    public Response<CategoryWebResponse> hide(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.hide(id)));
    }

    @PutMapping(value = "/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CategoryWebResponse> editById(@RequestBody CategoryWebRequest categoryWebRequest, @PathVariable String id) throws IOException {
        Category category = WebRequestConstructor.toCategoryEntity(categoryWebRequest);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(categoryService.save(category, id)));
    }

    @DeleteMapping(value = "/api/categories/{id}")
    public void deleteById(@PathVariable String id){
        categoryService.deleteById(id);
    }
}
