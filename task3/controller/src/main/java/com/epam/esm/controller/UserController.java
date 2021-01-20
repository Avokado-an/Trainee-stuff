package com.epam.esm.controller;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateUserDto;
import com.epam.esm.entity.dto.MostPopularTagDto;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private TagService tagService;
    private OrderService orderService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
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

    @PostMapping("{userId}/orders/popular_tag")
    public List<Tag> viewMostUsedTag(@PathVariable String userId) {
        return tagService.findMostUsedUserTag(userId);
    }

    @PostMapping("{userId}/orders/max_price")
    public List<CertificateOrder> viewMostExpensiveOrder(@PathVariable String userId) {
        return orderService.findMostExpensiveUserOrder(userId);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public Optional<CertificateOrder> viewUserOrderById(@PathVariable String userId, @PathVariable String orderId) {
        return orderService.findUserOrderById(userId, orderId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorHandler handleIncorrectParameterValueException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND);
    }
}
