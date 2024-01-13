package com.learning.dscatalog.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.entities.Category;
import com.learning.dscatalog.entities.Product;


@Service
public class MapperService {
    
    private final ModelMapper modelMapper;

    public MapperService(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        configureProductMappings();

    }

    private void configureProductMappings() {
        TypeMap<Product, ProductDTO> productTypeMap = modelMapper.createTypeMap(Product.class, ProductDTO.class);
        productTypeMap.addMapping(x -> x.getCategories(), ProductDTO :: setCategories);
    }

    public ProductDTO productToDto(Product product){
        return modelMapper.map(product, ProductDTO.class);
    }

    public CategoryDTO categoryToDto(Category category){
        return modelMapper.map(category, CategoryDTO.class);
    }
}
     