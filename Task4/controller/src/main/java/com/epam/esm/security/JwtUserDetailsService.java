package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.loadUserByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException("username " + username + " is not present");
        }
        return JwtUserFactory.create(user);
    }
}
