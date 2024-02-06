package com.learning.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.entities.Category;
import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.repositories.CategoryRepository;
import com.learning.dscatalog.repositories.ProductRepository;
import com.learning.dscatalog.services.exceptions.DatabaseException;
import com.learning.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MapperService mapper;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, String category, Pageable pageable) {
        Page<Product> products = productRepository.searchProductByNameAndOrCategory(name, category, pageable);
        return products.map(x -> mapper.productToDto(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        return mapper.productToDto(product);
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product();
        copyDtoToEntity(productDTO, product);
        productRepository.save(product);
        return mapper.productToDto(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(productDTO, product);
            productRepository.save(product);
            return mapper.productToDto(product);
        } 
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!productRepository.existsById(id))
            throw new ResourceNotFoundException("Resource not found: " + id);
        try {
            productRepository.deleteById(id);
        } 
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity constraint violation");
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImgUrl(productDTO.getImgUrl());
        product.setPrice(productDTO.getPrice());
        product.setDate(productDTO.getDate());
        product.getCategories().clear();
        for (CategoryDTO catDto : productDTO.getCategories()){
            Category cat = categoryRepository.findById(catDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + catDto.getId()));
            product.getCategories().add(cat);
        }
    }
}
