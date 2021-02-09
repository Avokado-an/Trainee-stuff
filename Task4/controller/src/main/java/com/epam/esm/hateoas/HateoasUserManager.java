package com.epam.esm.hateoas;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.representation.GiftCertificateRepresentationDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.dto.representation.UserRepresentationDto;
import com.epam.esm.exception.ResultNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasUserManager {
    private static final String ORDERS = "orders";
    private static final String ORDER = "order";
    private static final String USER = "user";
    private static final String USERS = "users";
    private static final String CERTIFICATE = "certificate";

    private HateoasUserManager() {
    }

    public static void manageAllUsersLinks(Page<UserRepresentationDto> users, Pageable pageable)
            throws ResultNotFoundException {
        for (UserRepresentationDto user : users) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewAllUsers(pageable)).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserById(user.getId().toString())).withRel(USER);
            Link userOrdersLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserOrders(user.getId().toString(), pageable)).withRel(ORDERS);
            user.add(selfLink, userLink, userOrdersLink);
        }
    }

    public static void manageAnyUserLinks(UserRepresentationDto user) throws ResultNotFoundException {
            Link usersLink = linkTo(methodOn(UserController.class)
                    .viewAllUsers(Pageable.unpaged())).withRel(USERS);
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserById(user.getId().toString())).withSelfRel();
            Link userOrdersLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserOrders(user.getId().toString(), Pageable.unpaged())).withRel(ORDERS);
            user.add(usersLink, selfLink, userOrdersLink);
    }

    public static void manageAnyUserOrdersLinks(Page<OrderRepresentationDto> orders, String userId, Pageable pageable)
            throws ResultNotFoundException {
        for (OrderRepresentationDto order : orders) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserOrders(userId, pageable)).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserById(userId)).withRel(USER);
            Link userOrderLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserOrderById(userId, order.getId().toString())).withRel(ORDER);
            order.add(selfLink, userLink, userOrderLink);
        }
    }

    public static void manageAnyUserOrderLinks(OrderRepresentationDto order, String userId) throws ResultNotFoundException {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserOrderById(userId, order.getId().toString())).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserById(userId)).withRel(USER);
            Link userOrdersLink = linkTo(methodOn(UserController.class)
                    .viewAnyUserOrders(userId, Pageable.unpaged())).withRel(ORDERS);
            for (GiftCertificateRepresentationDto certificate : order.getCertificates()) {
                Link certificateLink = linkTo(methodOn(GiftCertificateController.class)
                        .showCertificates(certificate.getId().toString())).withRel(CERTIFICATE);
                certificate.add(certificateLink);
            order.add(selfLink, userLink, userOrdersLink);
        }
    }
}
