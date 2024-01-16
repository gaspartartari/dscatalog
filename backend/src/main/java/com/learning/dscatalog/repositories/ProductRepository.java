package com.learning.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    
    @Query( value = "SELECT p.name AS name, p.description, p.price, p.img_url as imgUrl, p.date, c.id AS categoryId, c.name AS categoryName  FROM tb_product p "
            + "INNER JOIN tb_product_category pc ON p.id = pc.product_id "
            + "INNER JOIN tb_category c ON c.id = pc.category_id "
            + "WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', 'pc', '%')) "
            + "AND UPPER(c.name) LIKE UPPER(CONCAT('%', 'computer', '%')) "
            + "ORDER BY p.id", nativeQuery = true)
    Page<com.learning.dscatalog.projections.ProductProjection> searchProductByNameAndOrCategory(String name, String category, Pageable pageable);
    
}
