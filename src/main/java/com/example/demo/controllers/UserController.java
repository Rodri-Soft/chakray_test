package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Response;
import com.example.demo.models.User;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UserController {

  @Autowired
  UserService userService;

  @GetMapping("/findAll")
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping()
  public ResponseEntity<List<User>> getFilteredUsers(@RequestParam(required = false) String sortedBy) {

    try {
      if (sortedBy == null || sortedBy.isBlank()) {
        return ResponseEntity.ok(userService.getAllUsers());
      }

      return ResponseEntity.ok(userService.getSortedUsers(sortedBy));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/filter")
  public ResponseEntity<Response> getUsersByFilter(@RequestParam String filter) {

    Response response = new Response();

    try {

      if (filter == null || filter.isBlank()) {
        response.setMessage("Los filtros son obligatorios");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }

      String[] filters = filter.split("\\+");
      if (filters.length != 3) {
        response.setMessage("Los filtros son obligatorios");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }

      List<User> result = userService.getUsersByFilter(filters);
      if (result.isEmpty()) {
        response.setMessage("No se encontraron usuarios con los filtros proporcionados");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      response.message = "Usuarios encontrados correctamente";
      response.data = result;

      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

  }

  @PostMapping("save")
  public ResponseEntity<Response> saveUser(@RequestBody User user) {

    Response response = new Response();

    try {

      if (user.getEmail() == null || user.getEmail().isBlank()) {
        response.message = "El email es obligatorio";
        return ResponseEntity.badRequest().body(response);
      }
      if (user.getTaxId() == null || user.getTaxId().isBlank()) {
        response.message = "Tax_id es obligatorio";
        return ResponseEntity.badRequest().body(response);
      }
      if (userService.existsByTaxId(user.getTaxId())) {
        response.message = "El RFC ya existe en la base de datos";
        return ResponseEntity.badRequest().body(response);
      }

      User savedUser =userService.saveUser(user);
      response.message = "Usuario guardado correctamente";
      response.data = savedUser;
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
  }

}
