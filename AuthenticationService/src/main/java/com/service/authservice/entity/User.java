package com.service.authservice.entity;

import com.service.authservice.constant.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "username")
    private String username;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty @Pattern(regexp = "[0-9]{10}")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotEmpty
    @Column(name = "role")
    private String role = UserRole.ROLE_ADMIN.toString();

    @Column(name = "active")
    private boolean isActive = true;

    @Column(name = "expiry")
    private boolean isAccountNotExpired = true;

    @Column(name = "block")
    private boolean isAccountNotBlocked = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public boolean isPasswordExpired() {
        return createdAt.isAfter(LocalDateTime.now());
    }
}
