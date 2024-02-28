package com.learning.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.dscatalog.entities.Role;
import com.learning.dscatalog.entities.User;
import com.learning.dscatalog.projections.UserDetailsProjection;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(nativeQuery = true, value = """
            SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId,
            tb_role.authority
            FROM tb_user
            INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
            INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
            WHERE tb_user.email = :email
            """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

    @EntityGraph(attributePaths = { "roles" })
    @Query(value = "SELECT u FROM User u", countQuery = "SELECT COUNT(u) FROM User u")
    Page<User> findAllUsersWithRoles(Pageable pageable);

    

}
