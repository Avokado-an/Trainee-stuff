package com.epam.esm.controller;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateUserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<User> viewAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public Optional<User> createUser(@RequestBody CreateUserDto newUSer) {
        return userService.createUser(newUSer);
    }

    @GetMapping("/{userId}")
    public Optional<User> viewUserById(@PathVariable String userId) {
        return userService.findById(userId);
    }

    @GetMapping("/{userId}/orders")
    public List<CertificateOrder> viewUserOrders(@PathVariable String userId) {
        return orderService.findUserOrders(userId);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public Optional<CertificateOrder> viewUserOrderById(@PathVariable String userId, @PathVariable String orderId) {
        return orderService.findUserOrderById(userId, orderId);
    }
}
