package com.learning.dscatalog.factories;

import org.springframework.beans.BeanUtils;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.entities.Category;

public class CategoryFactory {


    public static Category createCategory(){
        
        return new Category(1L, "Electronics");
      
    }

    public static CategoryDTO creaCategoryDTO(){
        
        CategoryDTO categoryDTO = new CategoryDTO();
        Category category = createCategory();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
        
    }
}
