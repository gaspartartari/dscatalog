package com.learning.dscatalog.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.learning.dscatalog.DTO.FieldMessage;
import com.learning.dscatalog.DTO.RoleDTO;
import com.learning.dscatalog.DTO.UserUpdateDTO;
import com.learning.dscatalog.entities.User;
import com.learning.dscatalog.repositories.RoleRepository;
import com.learning.dscatalog.repositories.UserRepository;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private UserRepository userRepository;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();


		List<Long> roles = roleRepository.findAllRolesId();
        for(RoleDTO roleDto : dto.getRoles()){
            Long id = roleDto.getId();
            if(!roles.contains(id))
                list.add(new FieldMessage("roles", "Role id "+ roleDto.getId() + " does not exist"));
        }

		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Long userId = Long.parseLong(uriVars.get("id"));
		
		User user = userRepository.findByEmail(dto.getEmail());
		if( user != null && user.getId() != userId)
			list.add(new FieldMessage("email", "Email already exists"));
		
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}