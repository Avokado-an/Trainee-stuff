package com.epam.esm.util;

import com.epam.esm.entity.User;
import com.epam.esm.security.JwtAuthenticationException;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@Transactional
public class CurrentPrincipalDefiner {
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public User getPrincipal() {
        String username = currentUsername();
        return userService.loadUserByUsername(username);
    }

    public String currentUsername() {
        Optional<HttpServletRequest> request = getCurrentHttpRequest();
        if (request.isPresent()) {
            String token = jwtTokenProvider.resolveToken(request.get());
            return jwtTokenProvider.getUsername(token);
        } else {
            throw new JwtAuthenticationException("What the hell is happening: cant get username of current user");
        }
    }

    private Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }
}
