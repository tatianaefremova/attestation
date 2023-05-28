package com.example.bookshop.services;

import com.example.bookshop.models.Category;
import com.example.bookshop.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Category findByName(String name){
        return categoryRepository.findByName(name);
    }

    public Category getCategoryId(int id) {
        Optional<Category> optional = categoryRepository.findById(id);
        return optional.orElse(null);
    }
}
