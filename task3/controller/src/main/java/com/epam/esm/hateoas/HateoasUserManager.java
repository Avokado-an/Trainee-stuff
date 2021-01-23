package com.epam.esm.hateoas;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.UserController;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
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

    public static void manageAllUsersLinks(Page<User> users, Pageable pageable) {
        for (User user : users) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewAllUsers(pageable)).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class)
                    .viewUserById(user.getId().toString())).withRel(USER);
            Link userOrdersLink = linkTo(methodOn(UserController.class)
                    .viewUserOrders(user.getId().toString(), pageable)).withRel(ORDERS);
            user.add(selfLink, userLink, userOrdersLink);
        }
    }

    public static void manageUserLinks(Optional<User> user) {
        if (user.isPresent()) {
            Link usersLink = linkTo(methodOn(UserController.class)
                    .viewAllUsers(Pageable.unpaged())).withRel(USERS);
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewUserById(user.get().getId().toString())).withSelfRel();
            Link userOrdersLink = linkTo(methodOn(UserController.class)
                    .viewUserOrders(user.get().getId().toString(), Pageable.unpaged())).withRel(ORDERS);
            user.get().add(usersLink, selfLink, userOrdersLink);
        }
    }

    public static void manageUserOrdersLinks(Page<CertificateOrder> orders, String userId, Pageable pageable) {
        for (CertificateOrder order : orders) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewUserOrders(userId, pageable)).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class)
                    .viewUserById(userId)).withRel(USER);
            Link userOrderLink = linkTo(methodOn(UserController.class)
                    .viewUserOrderById(userId, order.getId().toString())).withRel(ORDER);
            order.add(selfLink, userLink, userOrderLink);
        }
    }

    public static void manageUserOrderLinks(Optional<CertificateOrder> order, String userId) {
        if (order.isPresent()) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .viewUserOrderById(userId, order.get().getId().toString())).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class)
                    .viewUserById(userId)).withRel(USER);
            Link userOrdersLink = linkTo(methodOn(UserController.class)
                    .viewUserOrders(userId, Pageable.unpaged())).withRel(ORDERS);
            for (GiftCertificate certificate : order.get().getCertificates()) {
                Link certificateLink = linkTo(methodOn(GiftCertificateController.class)
                        .showCertificates(certificate.getId().toString())).withRel(CERTIFICATE);
                certificate.add(certificateLink);
            }
            order.get().add(selfLink, userLink, userOrdersLink);
        }
    }
}
