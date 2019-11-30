package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Category;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(String id);

    Category hide(String id);

    Category save(Category category) throws IOException;

    Category save(Category category, String id) throws IOException;

    Page<Category> findPaginated(int page, int size);

    void deleteById(String id);
}
