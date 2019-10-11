package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.repositories.CategoryRepository;
import com.future.medan.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(String id){
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
    }

    @Override
    public Category save(Category category){
        return categoryRepository.save(category);
    }

    @Override
    public Category save(Category category, String id){
        if (!categoryRepository.existsById(id))
            throw new ResourceNotFoundException("Category", "id", id);
        else
            return categoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) { categoryRepository.deleteById(id); }
}
