package com.epam.esm.controller;

import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.dto.representation.TagRepresentationDto;
import com.epam.esm.dto.representation.UserRepresentationDto;
import com.epam.esm.entity.User;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.exception.ResultNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.CurrentPrincipalDefiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.esm.hateoas.HateoasUserManager.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private TagService tagService;
    private OrderService orderService;
    private CurrentPrincipalDefiner principalDefiner;

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

    @Autowired
    public void setPrincipalDefiner(CurrentPrincipalDefiner principalDefiner) {
        this.principalDefiner = principalDefiner;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public Page<UserRepresentationDto> viewAllUsers(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws ResultNotFoundException {
        Page<UserRepresentationDto> users = userService.findAllUsers(pageable);
        manageAllUsersLinks(users, pageable);
        return users;
    }

    @GetMapping("/current")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENT"})
    public Page<UserRepresentationDto> viewCurrentUser(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws ResultNotFoundException {
        Page<UserRepresentationDto> users = userService.findAllUsers(pageable);
        manageAllUsersLinks(users, pageable);
        return users;
    }

    @GetMapping("/{userId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserRepresentationDto> viewAnyUserById(@PathVariable String userId)
            throws ResultNotFoundException {
        UserRepresentationDto user = userService.findById(userId);
        manageAnyUserLinks(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/orders")
    @Secured("ROLE_ADMIN")
    public Page<OrderRepresentationDto> viewAnyUserOrders(@PathVariable String userId, @PageableDefault(
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws ResultNotFoundException {
        Page<OrderRepresentationDto> orders = orderService.findUserOrders(userId, pageable);
        manageAnyUserOrdersLinks(orders, userId, pageable);
        return orders;
    }

    @GetMapping("/orders")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENT"})
    public Page<OrderRepresentationDto> viewCurrentUserOrders(@PageableDefault(
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws ResultNotFoundException {
        User user = principalDefiner.getPrincipal();
        Page<OrderRepresentationDto> orders = orderService.findUserOrders(user.getId().toString(), pageable);
        manageAnyUserOrdersLinks(orders, user.getId().toString(), pageable);
        return orders;
    }

    @PostMapping("{userId}/orders/popular-tag")
    @Secured("ROLE_ADMIN")
    public List<TagRepresentationDto> viewAnyUserMostUsedTag(@PathVariable String userId) {
        return tagService.findMostUsedUserTag(userId);
    }

    @PostMapping("{userId}/orders/max-price")
    @Secured("ROLE_ADMIN")
    public List<OrderRepresentationDto> viewAnyUserMostExpensiveOrder(@PathVariable String userId) {
        return orderService.findMostExpensiveUserOrder(userId);
    }

    @PostMapping("/orders/popular-tag")
    @Secured("ROLE_ADMIN")
    public List<TagRepresentationDto> viewCurrentUserMostUsedTag() {
        User user = principalDefiner.getPrincipal();
        return tagService.findMostUsedUserTag(user.getId().toString());
    }

    @PostMapping("/orders/max-price")
    @Secured("ROLE_ADMIN")
    public List<OrderRepresentationDto> viewCurrentUserMostExpensiveOrder() {
        User user = principalDefiner.getPrincipal();
        return orderService.findMostExpensiveUserOrder(user.getId().toString());
    }

    @GetMapping("/{userId}/orders/{orderId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<OrderRepresentationDto> viewAnyUserOrderById(
            @PathVariable String userId, @PathVariable String orderId) throws ResultNotFoundException {
        OrderRepresentationDto order = orderService.findUserOrderById(userId, orderId);
        manageAnyUserOrderLinks(order, userId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    @Secured({"ROLE_CLIENT", "ROLE_ADMIN"})
    public ResponseEntity<OrderRepresentationDto> viewCURRENTUserOrderById(@PathVariable String orderId)
            throws ResultNotFoundException {
        String userId = principalDefiner.getPrincipal().getId().toString();
        OrderRepresentationDto order = orderService.findUserOrderById(userId, orderId);
        manageAnyUserOrderLinks(order, userId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleResourceNotFoundException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }

    @ExceptionHandler({ResultNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleResourceNotFoundException(ResultNotFoundException exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }
}
