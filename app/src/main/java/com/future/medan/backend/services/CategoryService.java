package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAll();

    Optional<Category> getById(String id);

    Category save(Category category);

    void deleteById(String id);
}
