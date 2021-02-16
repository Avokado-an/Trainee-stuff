package com.epam.esm.service.impl;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.dto.representation.UserRepresentationDto;
import com.epam.esm.dto.status.OrderOperationStatus;
import com.epam.esm.entity.BankAcc;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.exception.ResultNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Transactional
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private OrderService orderService;
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
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public Page<UserRepresentationDto> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(u -> modelMapper.map(u, UserRepresentationDto.class));
    }

    @Override
    public Optional<UserRepresentationDto> createUser(CreateUserDto newUser) {
        Optional<UserRepresentationDto> createdUser;
        if (isUsernameFree(newUser.getUsername())) {
            User user = modelMapper.map(newUser, User.class);
            user.setPassword(encoder.encode(newUser.getPassword()));
            user.setMoneyAccount(BankAcc.builder().moneyAmount(0L).user(user).build());
            user.setRoles(new HashSet<>(Collections.singleton(UserRole.ROLE_CLIENT)));
            user.setEnabled(true);
            user.setCertificateOrders(new HashSet<>());
            createdUser = Optional.of(modelMapper.map(userRepository.save(user), UserRepresentationDto.class));
            createdUser.get().setOperationStatus("User was created");
        } else {
            createdUser = Optional.of(modelMapper.map(newUser, UserRepresentationDto.class));
            createdUser.get().setOperationStatus("Username is taken");
        }
        return createdUser;
    }

    @Override
    public OrderRepresentationDto makeOrder(String username, CreateOrderDto order) {
        OrderRepresentationDto createdOrder = orderService.createOrderInstance(order);
        User user = userRepository.findByUsername(username);
        if (nonNull(user)) {
            createdOrder = processOrder(user, order);
        } else {
            createdOrder.setStatus(OrderOperationStatus.INVALID_USER_INFO);
        }
        return createdOrder;
    }

    @Override
    public UserRepresentationDto findById(String id) throws ResultNotFoundException {
        UserRepresentationDto searchedUser;
        try {
            long userId = Long.parseLong(id);
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                searchedUser = modelMapper.map(user.get(), UserRepresentationDto.class);
            } else {
                throw new ResultNotFoundException("User was not found");
            }
        } catch (NumberFormatException e) {
            throw new ResultNotFoundException("Invalid id (should contain only numbers)");
        }
        return searchedUser;
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private boolean isUsernameFree(String username) {
        return isNull(userRepository.findByUsername(username));
    }

    private OrderRepresentationDto processOrder(User user, CreateOrderDto order) {
        OrderRepresentationDto createdOrder = orderService.createOrderInstance(order);
        long totalPrice = orderService.calculateTotalPrice(order.getOrderedCertificatesId());
        long userMoney = user.getMoneyAccount().getMoneyAmount();
        if (totalPrice <= userMoney) {
            createdOrder = orderService.create(user, order);
            if (createdOrder.getStatus().equals(OrderOperationStatus.CREATED))
                user.addOrder(modelMapper.map(createdOrder, CertificateOrder.class));
            user.getMoneyAccount().setMoneyAmount(userMoney - totalPrice);
            userRepository.save(user);
        } else {
            createdOrder.setStatus(OrderOperationStatus.NOT_ENOUGH_MONEY);
        }
        return createdOrder;
    }
}
