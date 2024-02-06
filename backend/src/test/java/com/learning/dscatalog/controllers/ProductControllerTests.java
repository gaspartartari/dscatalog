package com.learning.dscatalog.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.factories.ProductFactory;
import com.learning.dscatalog.services.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService producService;

    private ProductDTO productDto;

    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setup() throws Exception {

        productDto = ProductFactory.createProductDto();
        page = new PageImpl<>(List.of(productDto));
        Pageable pageable = PageRequest.of(0, 10);

        when(producService.findAll(eq(""), eq(""),eq(pageable))).thenReturn(page);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{
        
        ResultActions result = mockMvc.perform(get("/products")
            .accept(MediaType.APPLICATION_JSON));
        
        result.andExpect(status().isOk());
                
    }
    
}
