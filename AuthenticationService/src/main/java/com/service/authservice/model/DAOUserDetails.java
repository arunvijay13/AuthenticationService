package com.service.authservice.model;

import com.service.authservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DAOUserDetails implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> authorityList;
    private boolean isAccountNotExpired;
    private boolean isAccountNotLocked;
    private boolean isEnabled;

    public DAOUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.isAccountNotExpired = user.isAccountNotExpired();
        this.isAccountNotLocked = user.isAccountNotBlocked();
        this.isEnabled = user.isActive();
        this.authorityList = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
