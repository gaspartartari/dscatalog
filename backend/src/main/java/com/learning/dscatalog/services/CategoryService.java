package com.learning.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.entities.Category;
import com.learning.dscatalog.repositories.CategoryRepository;
import com.learning.dscatalog.services.exceptions.DatabaseException;
import com.learning.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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
        Category cat = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        return mapper.categoryToDto(cat);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        copyDtoToEntity(categoryDTO, category);
        categoryRepository.save(category);
        return mapper.categoryToDto(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category cat = categoryRepository.getReferenceById(id);
            cat.setName(categoryDTO.getName());
            categoryRepository.save(cat);
            return mapper.categoryToDto(cat);
        } 
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        try {
            categoryRepository.deleteById(id);
        } 
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity constraint violation");
        }
    }

    private void copyDtoToEntity(CategoryDTO categoryDTO, Category category) {
        category.setName(categoryDTO.getName());
    }
}
