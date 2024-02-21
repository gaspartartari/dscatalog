package com.learning.dscatalog.DTO;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;



public class UserDTO {
    
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Last name cannot be empty")
    private String email;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(){

    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
