package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String name;

    @Pattern(regexp = "^(\\+\\d{1,3}\\s?)?\\d{10}$", message = "Formato de teléfono inválido")
    private String phone;

    @JsonIgnore
    @Column(nullable = false)
    private byte[] password;

    @Pattern(regexp = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$", message = "Formato de RFC inválido")
    @Length(min = 12, max = 13)
    @Column(nullable = false, unique = true)
    private String taxId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;    
}