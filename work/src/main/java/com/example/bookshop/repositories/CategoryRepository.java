package com.example.bookshop.repositories;

import com.example.bookshop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    com.example.bookshop.models.Category findByName(String name);
}
