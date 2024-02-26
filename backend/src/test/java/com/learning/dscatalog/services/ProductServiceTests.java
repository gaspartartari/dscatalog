package com.learning.dscatalog.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.entities.Category;
import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.factories.ProductFactory;
import com.learning.dscatalog.projections.ProductProjection;
import com.learning.dscatalog.repositories.CategoryRepository;
import com.learning.dscatalog.repositories.ProductRepository;
import com.learning.dscatalog.services.exceptions.DatabaseException;
import com.learning.dscatalog.services.exceptions.ResourceNotFoundException;
import com.learning.dscatalog.factories.CategoryFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl page;
    private Product product;
    private ProductDTO productDto;
    private Category category;
    private CategoryDTO categoryDTO;
    private List<Long> categoryIds = new ArrayList<>();

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MapperService mapper;

    @BeforeEach
    void setup() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = ProductFactory.createProduct();
        productDto = ProductFactory.createProductDto();
        category = CategoryFactory.createCategory();
        page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);
        categoryIds.add(1L);
        categoryIds.add(2L);
        categoryIds.add(3L);

        

        doNothing().when(productRepository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.existsById(nonExistingId)).thenReturn(false);
        when(productRepository.existsById(dependentId)).thenReturn(true);
        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        when(productRepository.getReferenceById(dependentId)).thenReturn(product);
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(categoryRepository.findById(existingId)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(productRepository.searchProductByNameAndOrCategory("", categoryIds, pageable)).thenReturn(page);
        when(mapper.productToDto(any(Product.class))).thenReturn(productDto);

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = productService.update(nonExistingId, productDto);
        });

        verify(productRepository).getReferenceById(nonExistingId);

    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists(){

        Assertions.assertNotNull(productDto, "productDto should not be null after initialization");

        ProductDTO result = productService.update(existingId, productDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productDto.getId(), result.getId());
        Assertions.assertEquals(productDto.getName(), result.getName());
        Assertions.assertEquals(productDto.getDescription(), result.getDescription());
        Assertions.assertEquals(productDto.getPrice(), result.getPrice());
        Assertions.assertEquals(productDto.getImgUrl(), result.getImgUrl());
        Assertions.assertEquals(productDto.getDate(), result.getDate());
        Set<CategoryDTO> resultCategories = result.getCategories();
        Set<CategoryDTO> productDtoCategories = productDto.getCategories();
        Assertions.assertEquals(productDtoCategories.size(), resultCategories.size());

        for (CategoryDTO resultCat : resultCategories) {
            Assertions.assertTrue(productDtoCategories.stream().anyMatch(productDtoCat -> productDtoCat.getId().equals(resultCat.getId())));
            Assertions.assertTrue(productDtoCategories.stream().anyMatch(productDtoCat -> productDtoCat.getName().equals(resultCat.getName())));
        }
    
        verify(productRepository).getReferenceById(existingId);
        verify(productRepository).save(product);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = productService.findById(nonExistingId);
        });

        verify(productRepository).findById(nonExistingId);

    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists(){

        ProductDTO result = productService.findById(existingId);

        Assertions.assertNotNull(result);

        verify(productRepository).findById(existingId);
    }

   

    

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsConstrained() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }
}
