package com.learning.dscatalog.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.repositories.ProductRepository;

@Service
public class ProductService {

    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MapperService mapper;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, String category, Pageable pageable) {
        Page<Product> products = productRepository.searchProductByNameAndOrCategory(name, category, pageable);
        return products.map(x -> mapper.productToDto(x));
    }
    
}
