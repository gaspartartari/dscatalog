package com.learning.dscatalog.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.dscatalog.DTO.CategoryDTO;
import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.factories.ProductFactory;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private Long countTotalProductsFromComputerCategory;


    @BeforeEach
    void setup() throws Exception{
       
        existingId = 1l;
        nonExistingId = 10000L;
        countTotalProducts = 25L;
        countTotalProductsFromComputerCategory = 23L;
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {

        ResultActions result = mockMvc.perform(get("/products?sort=name,asc")
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void findAllShouldReturnPageWithProductsOfComputerCategoryWhenSortByCategoryParamIsComputer() throws Exception {

        ResultActions result = mockMvc.perform(get("/products?category=computers")
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalProductsFromComputerCategory));
        result.andExpect(jsonPath("$.content").exists());
       
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

        ProductDTO productDto = ProductFactory.createProductDto();
        String expectedName = productDto.getName();
        String expectedDescription = productDto.getDescription();
        String expectedImgUrl = productDto.getImgUrl();
        Double expectedPrice = productDto.getPrice();
        Set<CategoryDTO> expectedCategories = productDto.getCategories();
        List<String> expectedCategoriesName = new ArrayList<>();
        for(CategoryDTO cat : expectedCategories){
            String catName = cat.getName();
            expectedCategoriesName.add(catName);
        }

        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
        result.andExpect(jsonPath("$.imgUrl").value(expectedImgUrl));
        result.andExpect(jsonPath("$.price").value(expectedPrice));
        result.andExpect(jsonPath("$.categories[0].name").value(expectedCategoriesName.get(0)));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ProductDTO productDto = ProductFactory.createProductDto();
        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

}
