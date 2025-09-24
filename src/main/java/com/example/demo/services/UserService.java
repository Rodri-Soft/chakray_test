package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  public ArrayList<User> getAllUsers() {
    return (ArrayList<User>) userRepository.findAll();
  }
  
}
