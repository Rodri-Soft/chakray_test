package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import com.example.demo.models.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String street;

  @Length(min = 2, max = 2)
  private String countryCode;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;
}
