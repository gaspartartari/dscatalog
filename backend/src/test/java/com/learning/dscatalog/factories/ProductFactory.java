package com.learning.dscatalog.factories;

import java.time.Instant;

import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.entities.Category;
import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.projections.ProductProjection;

public class ProductFactory {


    public static Product createProduct(){
        
        Product product = new Product(1L, "New Product", "Description", 89.00, "www.imgurl.com", Instant.parse("2022-02-28T03:30:00Z"));
        product.getCategories().add(new Category(1L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDto(){
        
        Product product = createProduct();
        ProductDTO productDto = new ProductDTO();
        BeanUtils.copyProperties(product, productDto);
        for(Category cat : product.getCategories()){
            CategoryDTO categoryDto = new CategoryDTO();
            categoryDto.setId(cat.getId());
            categoryDto.setName(cat.getName());
            productDto.getCategories().add(categoryDto);
        }
        return productDto;
        
    }
}
