package com.learning.dscatalog.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Name canot be blank")
    @Size(min = 10, message = "Name has to have at least 2 characters")
    private String name;

    public CategoryDTO(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
