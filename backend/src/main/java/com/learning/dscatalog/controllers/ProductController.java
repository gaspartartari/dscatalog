package com.learning.dscatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
    
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(name = "name", defaultValue = "") String name, 
            @RequestParam(name = "category", defaultValue = "") String category, 
            Pageable pageable){
        Page<ProductDTO> dto = productService.findAll(name, category, pageable);
        return ResponseEntity.ok(dto);
    }
     
}
