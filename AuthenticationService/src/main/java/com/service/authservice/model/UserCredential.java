package com.service.authservice.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserCredential {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
