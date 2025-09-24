package com.example.demo.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UserController {

  @Autowired
  UserService userService;

  @RequestMapping
  public ArrayList<User> getAllUsers() {
    return userService.getAllUsers(); 
  }

}
