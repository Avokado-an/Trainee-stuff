package com.epam.esm.service.impl;

import com.epam.esm.entity.BankAcc;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.entity.dto.CreateUserDto;
import com.epam.esm.repository.BankAccRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private BankAccRepository bankAccRepository;
    private OrderService orderService;
    private UserValidator userValidator;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBankAccRepository(BankAccRepository bankAccRepository) {
        this.bankAccRepository = bankAccRepository;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<User> createUser(CreateUserDto newUser) {
        Optional<User> createdUser = Optional.empty();
        if (userValidator.validateUser(newUser)) {
            User user = modelMapper.map(newUser, User.class);
            user = userRepository.save(user);
            BankAcc bankAcc = createUserBankAcc(user);
            user.setMoneyAccount(bankAcc);
            user.setRoles(new HashSet<>(Collections.singleton(UserRole.CLIENT)));
            user.setCertificateOrders(new HashSet<>());
            createdUser = Optional.of(userRepository.save(user));
        }
        return createdUser;
    }

    @Override
    @Transactional
    public Optional<User> makeOrder(CreateOrderDto order) {
        Optional<User> user = userRepository.findById(order.getBuyerId());
        if (user.isPresent()) {
            long totalPrice = orderService.calculateTotalPrice(order.getOrderedCertificatesId());
            long userMoney = user.get().getMoneyAccount().getMoneyAmount();
            if (totalPrice <= userMoney) {
                Optional<CertificateOrder> createdOrder = orderService.create(order);
                if (createdOrder.isPresent()) {
                    user.get().add(createdOrder.get());
                    user.get().getMoneyAccount().setMoneyAmount(userMoney - totalPrice);
                    userRepository.save(user.get());
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String id) {
        Optional<User> searchedUser;
        try {
            long userId = Long.parseLong(id);
            searchedUser = userRepository.findById(userId);
        } catch (NumberFormatException e) {
            searchedUser = Optional.empty();
        }
        return searchedUser;
    }

    private BankAcc createUserBankAcc(User user) {
        BankAcc bankAcc = new BankAcc();
        bankAcc.setMoneyAmount(0L);
        bankAcc.setUser(user);
        return bankAccRepository.save(bankAcc);
    }
}
