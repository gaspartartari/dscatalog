package com.learning.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
