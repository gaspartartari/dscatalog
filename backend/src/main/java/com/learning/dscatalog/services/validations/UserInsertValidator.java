package com.learning.dscatalog.services.validations;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.learning.dscatalog.DTO.FieldMessage;
import com.learning.dscatalog.DTO.RoleDTO;
import com.learning.dscatalog.DTO.UserInsertDTO;
import com.learning.dscatalog.entities.Role;
import com.learning.dscatalog.entities.User;
import com.learning.dscatalog.repositories.RoleRepository;
import com.learning.dscatalog.repositories.UserRepository;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		User user = userRepository.findByEmail(dto.getEmail());
        if (user != null){
            list.add(new FieldMessage("email", "Email already exsists"));
        }

        List<Long> roles = roleRepository.findAllRolesId();
        for(RoleDTO roleDto : dto.getRoles()){
            Long id = roleDto.getId();
            if(!roles.contains(id))
                list.add(new FieldMessage("roles", "Role id "+ roleDto.getId() + " does not exist"));
        }
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}