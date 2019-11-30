package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.repositories.CategoryRepository;
import com.future.medan.backend.services.CategoryService;
import com.future.medan.backend.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private StorageService storageService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, StorageService storageService) {
        this.categoryRepository = categoryRepository;
        this.storageService = storageService;
    }

    @Override
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(String id){
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    public Category save(Category category) throws IOException {
        String imagePath = storageService.storeImage(category.getImage(), UUID.randomUUID().toString());

        category.setImage(imagePath);

        return categoryRepository.save(category);
    }

    @Override
    public Category save(Category category, String id) throws IOException {
        if (!categoryRepository.existsById(id))
            throw new ResourceNotFoundException("Category", "id", id);
        else {
            String imagePath = storageService.storeImage(category.getImage(), UUID.randomUUID().toString());
            category.setImage(imagePath);
            category.setId(id);

            return categoryRepository.save(category);
        }
    }

    @Override
    public Category hide(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setHidden(!category.getHidden());

        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        if (!categoryRepository.existsById(id))
            throw new ResourceNotFoundException("Category", "id", id);
        else
            categoryRepository.deleteById(id);
    }
}
