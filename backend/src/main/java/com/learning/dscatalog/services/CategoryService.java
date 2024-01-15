package com.learning.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.entities.Category;
import com.learning.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MapperService mapper;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(x -> mapper.categoryToDto(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category cat = categoryRepository.findById(id).get();
        return mapper.categoryToDto(cat);
    }

    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        copyDtoToEntity(categoryDTO, category);
        categoryRepository.save(category);
        return mapper.categoryToDto(category);
    }

    private void copyDtoToEntity(CategoryDTO categoryDTO, Category category) {
        category.setName(categoryDTO.getName());
    }
}
