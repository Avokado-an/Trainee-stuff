package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {
    JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getPassword(), user.getUsername(),
                user.isEnabled(), defineAuthorities(user.getRoles()));
    }

    private static Set<SimpleGrantedAuthority> defineAuthorities(Set<UserRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());
    }
}
