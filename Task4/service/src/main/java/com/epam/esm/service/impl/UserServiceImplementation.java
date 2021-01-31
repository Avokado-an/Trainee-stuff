package com.epam.esm.service.impl;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.dto.representation.UserRepresentationDto;
import com.epam.esm.entity.BankAcc;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private OrderService orderService;
    private UserValidator userValidator;
    private ModelMapper modelMapper;
    private PasswordEncoder encoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public Page<UserRepresentationDto> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(u -> modelMapper.map(u, UserRepresentationDto.class));
    }

    @Override
    @Transactional
    public Optional<UserRepresentationDto> createUser(CreateUserDto newUser) {
        Optional<UserRepresentationDto> createdUser = Optional.empty();
        if (userValidator.validateUser(newUser)) {
            User user = modelMapper.map(newUser, User.class);
            user.setPassword(encoder.encode(newUser.getPassword()));
            user.setMoneyAccount(BankAcc.builder().moneyAmount(0L).user(user).build());
            user.setRoles(new HashSet<>(Collections.singleton(UserRole.CLIENT)));
            user.setEnabled(true);
            user.setCertificateOrders(new HashSet<>());
            createdUser = Optional.of(modelMapper.map(userRepository.save(user), UserRepresentationDto.class));
        }
        return createdUser;
    }

    @Override
    @Transactional
    public Optional<OrderRepresentationDto> makeOrder(CreateOrderDto order) {
        Optional<User> user = userRepository.findById(order.getBuyerId());
        Optional<OrderRepresentationDto> createdOrder = Optional.empty();
        if (user.isPresent()) {
            long totalPrice = orderService.calculateTotalPrice(order.getOrderedCertificatesId());
            long userMoney = user.get().getMoneyAccount().getMoneyAmount();
            if (totalPrice <= userMoney) {
                createdOrder = orderService.create(order);
                if (createdOrder.isPresent()) {
                    user.get().add(modelMapper.map(createdOrder.get(), CertificateOrder.class));
                    user.get().getMoneyAccount().setMoneyAmount(userMoney - totalPrice);
                    userRepository.save(user.get());
                }
            }
        }
        return createdOrder;
    }

    @Override
    @Transactional
    public Optional<UserRepresentationDto> findById(String id) {
        Optional<UserRepresentationDto> searchedUser = Optional.empty();
        try {
            long userId = Long.parseLong(id);
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                searchedUser = Optional.of(modelMapper.map(user.get(), UserRepresentationDto.class));
            }
        } catch (NumberFormatException e) {
            searchedUser = Optional.empty();
        }
        return searchedUser;
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
