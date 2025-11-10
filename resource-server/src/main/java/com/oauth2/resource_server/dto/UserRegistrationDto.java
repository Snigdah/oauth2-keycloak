package com.oauth2.resource_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String department;
    private List<String> roles;
}
