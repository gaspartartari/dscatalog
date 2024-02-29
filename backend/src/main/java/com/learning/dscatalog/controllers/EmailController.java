package com.learning.dscatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dscatalog.DTO.EmailDTO;
import com.learning.dscatalog.DTO.NewPasswordDTO;
import com.learning.dscatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class EmailController {
    
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO dto){
        authService.createRecoverToken(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/new-password")
    public ResponseEntity<Void> createNewPassword(@Valid @RequestBody NewPasswordDTO dto){
        authService.createNewPassword(dto);
        return ResponseEntity.noContent().build();
    }
}
