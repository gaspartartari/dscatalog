package com.learning.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.projections.ProductProjection;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, value = """
            SELECT DISTINCT p.id, p.name FROM tb_product p
            INNER JOIN tb_product_category pc ON p.id = pc.product_id
            WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%'))
            AND (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
                     """, countQuery = """
            SELECT COUNT(*) FROM (
            SELECT DISTINCT p.id, p.name FROM tb_product p
            INNER JOIN tb_product_category pc ON p.id = pc.product_id
            WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%'))
            AND (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
            ) As tb_result
                     """)
    Page<ProductProjection> searchProductByNameAndOrCategory(String name, List<Long> categoryIds, Pageable pageable);

    @Query(value = "SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productIds ORDER BY obj.name")
    List<Product> searchProductsWithCategories(List<Long> productIds);

}
