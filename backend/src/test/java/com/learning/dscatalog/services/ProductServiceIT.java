package com.learning.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.repositories.ProductRepository;
import com.learning.dscatalog.services.exceptions.ResourceNotFoundException;


@SpringBootTest
@Transactional
public class ProductServiceIT {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    
    @BeforeEach
    void setup() throws Exception{

        existingId = 1l;
        nonExistingId = 10000L;
        countTotalProducts = 25L;
    
    }

    @Test
    public void findAllShouldReturnPageWhenPageIs0andSizeIs10(){

        String name = "";
        String category = "";
        PageRequest pageRequest = PageRequest.of(2, 10);

        Page<ProductDTO> result = productService.findAll(name, category, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.getNumber());
        Assertions.assertEquals(5, result.getNumberOfElements());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName(){

        String name = "";
        String category = "";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = productService.findAll(name, category, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getNumberOfElements());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    
    }

    @Test  
    public void deleteShouldDeleteResourceWhenIdExists(){

        productService.delete(existingId);

        Assertions.assertEquals(countTotalProducts -1, productRepository.count());
    }

    @Test  
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }
}
