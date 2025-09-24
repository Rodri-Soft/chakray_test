package com.example.demo.services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.models.User.UpdateUserDTO;
import com.example.demo.models.User.User;
import com.example.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Value("${security.aes.secret}")
  private String secretKey;

  @Value("${security.default.password}")
  private String defaultPassword;

  public List<User> getAllUsers() {
    return (List<User>) userRepository.findAll();
  }

  public List<User> getSortedUsers(String sortedBy) {

    List<User> users = (List<User>) userRepository.findAll();
    return users.stream()
        .sorted(getComparator(sortedBy))
        .collect(Collectors.toList());
  }

  public List<User> getUsersByFilter(String[] filters) {

    String attribute = filters[0];
    String operator = filters[1];
    String value = filters[2];

    List<User> allUsers = (List<User>) userRepository.findAll();

    return allUsers.stream()
        .filter(user -> matchFilter(user, attribute, operator, value))
        .toList();
  }

  public User saveUser(User user) {

    user.setPassword(encryptPassword(defaultPassword));

    user.setCreatedAt(LocalDateTime.now());

    User savedUser = userRepository.save(user);

    savedUser.setPassword(null);

    return savedUser;
  }

  @Transactional
  public Optional<User> updateUser(String id, UpdateUserDTO dto) {

    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("No se encuentra el usuario");
    }

    return userOptional.map(user -> {
      if (dto.getEmail() != null)
        user.setEmail(dto.getEmail());
      if (dto.getName() != null)
        user.setName(dto.getName());
      if (dto.getPhone() != null)
        user.setPhone(dto.getPhone());
      if (dto.getTaxId() != null)
        user.setTaxId(dto.getTaxId());

      if (dto.getOldPassword() != null && dto.getNewPassword() != null) {

        String userPassword = decryptPassword(user.getPassword());

        if (!dto.getOldPassword().equals(userPassword)) {
          throw new IllegalArgumentException("La contraseña anterior no es correcta");
        }
        user.setPassword(encryptPassword(dto.getNewPassword()));
      }

      return userRepository.save(user);
    });
  }

  public boolean deleteUserById(String id) {
    Optional<User> userOpt = userRepository.findById(id);
    if (userOpt.isPresent()) {
      userRepository.delete(userOpt.get());
      return true;
    }
    return false;
  }

  private Comparator<User> getComparator(String sortedBy) {
    return switch (sortedBy) {
      case "id" -> Comparator.comparing(User::getId);
      case "email" -> Comparator.comparing(User::getEmail);
      case "name" -> Comparator.comparing(User::getName);
      case "phone" -> Comparator.comparing(User::getPhone);
      case "tax_id" -> Comparator.comparing(User::getTaxId);
      case "created_at" -> Comparator.comparing(User::getCreatedAt);
      default -> Comparator.comparing(User::getName);
    };
  }

  private boolean matchFilter(User user, String attribute, String operator, String value) {
    String attrValue;

    switch (attribute) {
      case "id" -> attrValue = user.getId();
      case "email" -> attrValue = user.getEmail();
      case "name" -> attrValue = user.getName();
      case "phone" -> attrValue = user.getPhone();
      case "tax_id" -> attrValue = user.getTaxId();
      case "created_at" -> attrValue = user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      default -> throw new IllegalArgumentException("Campo desconocido: " + attribute);
    }

    return switch (operator) {
      case "co" -> attrValue.contains(value);
      case "eq" -> attrValue.equals(value);
      case "sw" -> attrValue.startsWith(value);
      case "ew" -> attrValue.endsWith(value);
      default -> throw new IllegalArgumentException("Filtro desconocido: " + operator);
    };
  }

  public boolean existsByTaxId(String taxId) {
    return userRepository.existsByTaxId(taxId);
  }

  public byte[] encryptPassword(String password) {
    try {
      SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      byte[] iv = new byte[12];
      new java.security.SecureRandom().nextBytes(iv);
      GCMParameterSpec spec = new GCMParameterSpec(128, iv);

      cipher.init(Cipher.ENCRYPT_MODE, key, spec);

      byte[] encrypted = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));

      byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
      System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
      System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);

      return encryptedWithIv;

    } catch (Exception e) {
      throw new RuntimeException("Error al encriptar la contraseña: " + e.getMessage(), e);
    }
  }

  public String decryptPassword(byte[] encryptedWithIv) {
    try {

      byte[] iv = Arrays.copyOfRange(encryptedWithIv, 0, 12);

      byte[] encrypted = Arrays.copyOfRange(encryptedWithIv, 12, encryptedWithIv.length);

      SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(128, iv);

      cipher.init(Cipher.DECRYPT_MODE, key, spec);

      byte[] decrypted = cipher.doFinal(encrypted);
      return new String(decrypted, StandardCharsets.UTF_8);

    } catch (Exception e) {
      throw new RuntimeException("Error al desencriptar la contraseña: " + e.getMessage(), e);
    }
  }

}
