package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Response;
import com.example.demo.models.User.SaveUserDTO;
import com.example.demo.models.User.UpdateUserDTO;
import com.example.demo.models.User.User;
import com.example.demo.services.UserService;
import com.example.demo.services.UserValidator;

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

  @PostMapping("/")
  public ResponseEntity<Response> saveUser(@RequestBody SaveUserDTO user) {

    Response response = new Response();

    try {

      UserValidator userValidator = new UserValidator();
      userValidator.validateSaveUserDTO(user);

      Boolean existTax = userService.existsByTaxId(user.getTaxId());
      if (existTax) {
        response.message = "Ya existe un usuario con el mismo RFC";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }

      User newUser = new User();
      newUser.setEmail(user.getEmail());
      newUser.setName(user.getName());
      newUser.setPhone(user.getPhone());
      newUser.setTaxId(user.getTaxId());

      User savedUser = userService.saveUser(newUser);
      response.message = "Usuario guardado correctamente";
      response.data = savedUser;
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Response> updateUser(@RequestBody UpdateUserDTO user, @PathVariable String id) {

    Response response = new Response();

    try {

      UserValidator userValidator = new UserValidator();
      userValidator.validateUpdateUserDTO(user);

      Optional<User> userOptional = userService.updateUser(id, user);
      if (userOptional.isEmpty()) {
        response.message = "No se encuentra el usuario";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      response.message = "Usuario actualizado correctamente";
      response.data = userOptional.get();
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Response> deleteUser(@PathVariable String id) {

    Response response = new Response();

    try {
      boolean deleted = userService.deleteUserById(id);
      if (!deleted) {
        response.message = "No se encontró ningún usuario con id: " + id;
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(response);
      }
      response.message = "Usuario eliminado correctamente";
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      response.message = "Error al eliminar el usuario: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }
  }

}
