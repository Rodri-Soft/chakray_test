package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.User.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
  boolean existsByTaxId(String taxId);
  Optional<User> findUserById(String id);
}
