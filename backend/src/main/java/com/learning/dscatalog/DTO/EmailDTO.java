package com.learning.dscatalog.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email not valid")
    private String email;

    public EmailDTO(){

    }

    public EmailDTO(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
