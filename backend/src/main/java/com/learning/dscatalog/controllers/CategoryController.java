package com.learning.dscatalog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /* 
     public ResponseEntity<List<CategoryDTO> findAll(){
        List<CategoryDTO> dto = categoryService.findAll();
        return ResponseEntity.ok(dto);
    }
    */
    
    
}
