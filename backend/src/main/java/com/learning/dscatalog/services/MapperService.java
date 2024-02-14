package com.learning.dscatalog.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.DTO.UserDTO;
import com.learning.dscatalog.entities.Category;
import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.entities.User;
import com.learning.dscatalog.projections.ProductProjection;


@Service
public class MapperService {
    
    private final ModelMapper modelMapper;

    public MapperService(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        configureProductMappings();
        configureUserMappings();

    }

    private void configureProductMappings() {
        TypeMap<Product, ProductDTO> productTypeMap = modelMapper.createTypeMap(Product.class, ProductDTO.class);
        productTypeMap.addMapping(x -> x.getCategories(), ProductDTO :: setCategories);
    }

    private void configureUserMappings() {
        TypeMap<User, UserDTO> userTypeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
        userTypeMap.addMapping(x -> x.getRoles(), UserDTO :: setRoles);
    }

    public ProductDTO productToDto(Product product){
        return modelMapper.map(product, ProductDTO.class);
    }

    public CategoryDTO categoryToDto(Category category){
        return modelMapper.map(category, CategoryDTO.class);
    }

    public ProductDTO productProjectionToDto(ProductProjection projection){
        return modelMapper.map(projection, ProductDTO.class);
    }

    public UserDTO userToDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
     