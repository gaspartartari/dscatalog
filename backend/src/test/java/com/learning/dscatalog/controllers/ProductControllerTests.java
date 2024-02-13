package com.learning.dscatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.dscatalog.DTO.ProductDTO;
import com.learning.dscatalog.factories.ProductFactory;
import com.learning.dscatalog.services.ProductService;
import com.learning.dscatalog.services.exceptions.DatabaseException;
import com.learning.dscatalog.services.exceptions.ResourceNotFoundException;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDto;

    private PageImpl<ProductDTO> page;
    private List<ProductDTO> productList;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private Pageable pageable;

    @BeforeEach
    void setup() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDto = ProductFactory.createProductDto();
        productList = List.of(productDto); 
        page = new PageImpl<>(productList, PageRequest.of(0, 20), productList.size());
        pageable = PageRequest.of(0, 20);

        when(productService.findAll(any(String.class), any(String.class), any(Pageable.class))).thenReturn(page);
        when(productService.findById(existingId)).thenReturn(productDto);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(productService.update(eq(existingId), any())).thenReturn(productDto);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        doNothing().when(productService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependentId);
        when(productService.insert(any())).thenReturn(productDto);

    }

    @Test
    public void deleteShouldReturnNotFoundIfIdDoesNotExist() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
        Mockito.verify(productService).delete(nonExistingId);
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
        Mockito.verify(productService).delete(existingId);
    }

    @Test
    public void insertShouldReturnProductDto() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.categories").exists());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {

        String name = "";
        String category = "";
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        Mockito.verify(productService).findAll(name, category, pageable);

    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.categories").exists());

        Mockito.verify(productService).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

        Mockito.verify(productService).findById(nonExistingId);
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.categories").exists());

        Mockito.verify(productService).update(eq(existingId), any(ProductDTO.class));

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

        Mockito.verify(productService).update(eq(nonExistingId), any(ProductDTO.class));
    }

}
