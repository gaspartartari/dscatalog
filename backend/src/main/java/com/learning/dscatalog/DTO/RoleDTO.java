package com.learning.dscatalog.DTO;

public class RoleDTO {
    
    private Long id;
    private String authority;

    public RoleDTO(){

    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    
    
}
