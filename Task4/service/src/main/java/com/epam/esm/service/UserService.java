package com.epam.esm.service;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.dto.representation.UserRepresentationDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResultNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Page<UserRepresentationDto> findAllUsers(Pageable pageable);

    User loadUserByUsername(String username);

    Optional<UserRepresentationDto> createUser(CreateUserDto newUser);

    OrderRepresentationDto makeOrder(String username, CreateOrderDto order);

    UserRepresentationDto findById(String id) throws ResultNotFoundException;
}
