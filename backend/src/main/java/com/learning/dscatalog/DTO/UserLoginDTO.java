package com.learning.dscatalog.DTO;

public class UserLoginDTO extends UserDTO {
    
    private String password;

    public UserLoginDTO(){

    }

    public UserLoginDTO(String password) {
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
