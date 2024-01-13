package com.learning.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.dscatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
