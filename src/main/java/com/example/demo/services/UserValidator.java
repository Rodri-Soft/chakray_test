package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.models.User.SaveUserDTO;
import com.example.demo.models.User.UpdateUserDTO;

import java.util.regex.Pattern;

@Service
public class UserValidator {
    
    private static final Pattern RFC_PATTERN = Pattern.compile("^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$", Pattern.CASE_INSENSITIVE);    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public void validateSaveUserDTO(SaveUserDTO user) {
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (user.getPhone() == null || !PHONE_PATTERN.matcher(user.getPhone()).matches()) {
            throw new IllegalArgumentException("Teléfono inválido, debe tener al menos 10 dígitos");
        }

        if (user.getTaxId() == null || !RFC_PATTERN.matcher(user.getTaxId()).matches()) {
            throw new IllegalArgumentException("RFC (tax_id) inválido");
        }
    }

    public void validateUpdateUserDTO(UpdateUserDTO user) {
        if (user.getEmail() != null && !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (user.getPhone() != null && !PHONE_PATTERN.matcher(user.getPhone()).matches()) {
            throw new IllegalArgumentException("Teléfono inválido, debe tener al menos 10 dígitos");
        }

        if (user.getTaxId() != null && !RFC_PATTERN.matcher(user.getTaxId()).matches()) {
            throw new IllegalArgumentException("RFC (tax_id) inválido");
        }

        if (user.getOldPassword() != null) {
            if (user.getNewPassword() == null) {
                throw new IllegalArgumentException("Se requiere una nueva contraseña");
            }            
        }

        if (user.getNewPassword() != null) {
            if (user.getOldPassword() == null) {
                throw new IllegalArgumentException("Se requiere la contraseña anterior");
            }            
        }
    }
    



}
