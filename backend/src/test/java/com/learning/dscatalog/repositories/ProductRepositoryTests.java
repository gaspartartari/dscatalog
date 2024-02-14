package com.learning.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.factories.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private int totalProducts;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        nonExistingId = 10000L; 
        totalProducts = 25;
    }
    
    @Test
    public void deleteByIdShouldDeleteProductWhenIdExists(){
        
        productRepository.deleteById(existingId);
        Optional<Product> productIfExists = productRepository.findById(existingId);
        Assertions.assertFalse(productIfExists.isPresent());   
    }

    @Test
    public void saveShouldPersistObjectAndGenerateIdWhenIdNull(){

        Product product = ProductFactory.createProduct();
        product.setId(null);

        productRepository.save(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(totalProducts+1, product.getId());
    }

    @Test
    public void findByIdShouldReturnNotEmptyIfIdExists(){

        Optional<Product> product = productRepository.findById(existingId);
        Assertions.assertNotNull(product);
    }

    @Test
    public void findByIdShouldReturnEmptyIfIdDoesNotExists(){

        Optional<Product> product = productRepository.findById(nonExistingId);
        Assertions.assertFalse(product.isPresent());
    }
}
