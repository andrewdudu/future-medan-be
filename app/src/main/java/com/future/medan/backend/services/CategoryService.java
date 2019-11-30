package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(String id);

    Category hide(String id);

    Category save(Category category) throws IOException;

    Category save(Category category, String id) throws IOException;

    void deleteById(String id);
}
