package com.learning.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscatalog.DTO.RoleDTO;
import com.learning.dscatalog.DTO.UserDTO;
import com.learning.dscatalog.DTO.UserInsertDTO;
import com.learning.dscatalog.DTO.UserUpdateDTO;
import com.learning.dscatalog.entities.Role;
import com.learning.dscatalog.entities.User;
import com.learning.dscatalog.projections.UserDetailsProjection;
import com.learning.dscatalog.repositories.RoleRepository;
import com.learning.dscatalog.repositories.UserRepository;
import com.learning.dscatalog.services.exceptions.DatabaseException;
import com.learning.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MapperService mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if(result.size() == 0)
            throw new UsernameNotFoundException("username " + username + " not found");

        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAllUsersWithRoles(pageable);
        return users.map(x -> mapper.userToDto(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        return mapper.userToDto(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userLoginDTO) {
        User user = new User();
        copyDtoToEntity(userLoginDTO, user);
        user.setPassword(passwordEncoder.encode((userLoginDTO).getPassword()));
        userRepository.save(user);
        return mapper.userToDto(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        try {
            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(userUpdateDTO, user);
            userRepository.save(user);
            return mapper.userToDto(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("Resource not found: " + id);
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity constraint violation");
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        for (RoleDTO roleDto : userDTO.getRoles()) {
            Role role = roleRepository.findById(roleDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category " + roleDto.getId() + " not found"));
            user.getRoles().add(role);
        }
    }

   
}
