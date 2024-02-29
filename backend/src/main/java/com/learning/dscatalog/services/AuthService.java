package com.learning.dscatalog.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscatalog.DTO.EmailDTO;
import com.learning.dscatalog.entities.PasswordRecover;
import com.learning.dscatalog.entities.User;
import com.learning.dscatalog.repositories.PasswordRecoverRepository;
import com.learning.dscatalog.repositories.UserRepository;
import com.learning.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createRecoverToken(EmailDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null)
            throw new ResourceNotFoundException("Email not found");

        String token = UUID.randomUUID().toString();
        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(dto.getEmail());
        entity.setToken(token);
        entity.setTokenExpiration(Instant.now().plusSeconds(tokenMinutes * 60));

        entity = passwordRecoverRepository.save(entity);

        String body = "Access the link below to create a new password \n\n" +
                recoverUri + token + " Expires in " + tokenMinutes + " minutes";

        emailService.sendEmail(dto.getEmail(), "Password Recovery", body);

    }

}
