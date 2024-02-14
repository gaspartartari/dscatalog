package com.learning.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.dscatalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
