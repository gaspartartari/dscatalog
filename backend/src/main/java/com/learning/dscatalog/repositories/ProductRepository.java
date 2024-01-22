package com.learning.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {



    @Query(value = "SELECT p FROM Product p JOIN FETCH p.categories c "
            + "WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%')) "
            + "AND UPPER(c.name) LIKE UPPER(CONCAT('%', :category, '%'))", 
            countQuery = "SELECT COUNT(p) FROM Product p JOIN p.categories c "
            + "WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%')) "
            + "AND UPPER(c.name) LIKE UPPER(CONCAT('%', :category, '%'))")
    Page<Product> searchProductByNameAndOrCategory(String name, String category, Pageable pageable);

}
