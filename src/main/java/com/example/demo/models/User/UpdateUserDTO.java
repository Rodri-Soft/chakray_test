package com.example.demo.models.User;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {

  private String email;
  private String name;  
  private String phone;    
  private String taxId;  
  private String oldPassword;
  private String newPassword;
  
}
