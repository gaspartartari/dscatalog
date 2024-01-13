package com.learning.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    
    @Query("SELECT obj FROM Product obj JOIN obj.categories cat WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%')) "
            + "AND UPPER(cat.name) LIKE UPPER(CONCAT('%', :category, '%'))")
    Page<Product> searchProductByNameAndOrCategory(String name, String category, Pageable pageable);
    
}
