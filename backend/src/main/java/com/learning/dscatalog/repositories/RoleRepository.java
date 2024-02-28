package com.learning.dscatalog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.dscatalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
    @Query(value = "SELECT obj.id FROM Role obj")
    List<Long> findAllRolesId();


    Role findByAuthority(String string);
}
