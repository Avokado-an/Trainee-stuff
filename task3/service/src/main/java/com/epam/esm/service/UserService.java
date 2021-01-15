package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.entity.dto.CreateUserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    Optional<User> createUser(CreateUserDto newUser);

    Optional<User> makeOrder(CreateOrderDto order);

    Optional<User> findById(String id);
}
