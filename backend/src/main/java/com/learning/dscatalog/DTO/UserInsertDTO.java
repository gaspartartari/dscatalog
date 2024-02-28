package com.learning.dscatalog.DTO;

import com.learning.dscatalog.services.validations.UserInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", 
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character")
    private String password;

    public UserInsertDTO() {

    }

    public UserInsertDTO(String password) {
        super();
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
