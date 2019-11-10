package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(String id);

    Category save(Category category);

    Category save(Category category, String id);

    void deleteById(String id);
}
