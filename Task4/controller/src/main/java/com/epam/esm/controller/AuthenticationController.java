package com.epam.esm.controller;

import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.LoginDto;
import com.epam.esm.dto.representation.UserRepresentationDto;
import com.epam.esm.entity.User;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.SecurityService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private SecurityService securityService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        User user = securityService.loginUser(loginDto);
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/registration")
    public Optional<UserRepresentationDto> addUser(@Valid @RequestBody CreateUserDto profile) {
        return userService.createUser(profile);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleResourceNotFoundException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }

}
