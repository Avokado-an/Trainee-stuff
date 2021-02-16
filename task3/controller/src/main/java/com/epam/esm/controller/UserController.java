package com.epam.esm.controller;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateUserDto;
import com.epam.esm.entity.dto.representation.OrderRepresentationDto;
import com.epam.esm.entity.dto.representation.TagRepresentationDto;
import com.epam.esm.entity.dto.representation.UserRepresentationDto;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.hateoas.HateoasUserManager.*;

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
    public Page<UserRepresentationDto> viewAllUsers(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserRepresentationDto> users = userService.findAllUsers(pageable);
        manageAllUsersLinks(users, pageable);
        return users;
    }

    @PostMapping
    public Optional<UserRepresentationDto> createUser(@RequestBody CreateUserDto newUSer) {
        return userService.createUser(newUSer);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<UserRepresentationDto>> viewUserById(@PathVariable String userId) {
        Optional<UserRepresentationDto> user = userService.findById(userId);
        manageUserLinks(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/orders")
    public Page<OrderRepresentationDto> viewUserOrders(@PathVariable String userId, @PageableDefault(
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderRepresentationDto> orders = orderService.findUserOrders(userId, pageable);
        manageUserOrdersLinks(orders, userId, pageable);
        return orders;
    }

    @PostMapping("{userId}/orders/popular_tag")
    public List<TagRepresentationDto> viewMostUsedTag(@PathVariable String userId) {
        return tagService.findMostUsedUserTag(userId);
    }

    @PostMapping("{userId}/orders/max_price")
    public List<OrderRepresentationDto> viewMostExpensiveOrder(@PathVariable String userId) {
        return orderService.findMostExpensiveUserOrder(userId);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<Optional<OrderRepresentationDto>> viewUserOrderById(
            @PathVariable String userId, @PathVariable String orderId) {
        Optional<OrderRepresentationDto> order = orderService.findUserOrderById(userId, orderId);
        manageUserOrderLinks(order, userId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleResourceNotFoundException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }
}
