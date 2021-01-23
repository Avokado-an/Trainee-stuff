package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.entity.dto.CreateUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> findAllUsers(Pageable pageable);

    Optional<User> createUser(CreateUserDto newUser);

    Optional<User> makeOrder(CreateOrderDto order);

    Optional<User> findById(String id);

    Optional<User> findById(Long id);
}
