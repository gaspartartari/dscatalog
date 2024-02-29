package com.learning.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.dscatalog.entities.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {


}
