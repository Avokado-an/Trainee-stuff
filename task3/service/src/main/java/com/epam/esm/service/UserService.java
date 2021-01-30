package com.epam.esm.service;

import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.entity.dto.CreateUserDto;
import com.epam.esm.entity.dto.representation.OrderRepresentationDto;
import com.epam.esm.entity.dto.representation.UserRepresentationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<UserRepresentationDto> findAllUsers(Pageable pageable);

    Optional<UserRepresentationDto> createUser(CreateUserDto newUser);

    Optional<OrderRepresentationDto> makeOrder(CreateOrderDto order);

    Optional<UserRepresentationDto> findById(String id);
}
