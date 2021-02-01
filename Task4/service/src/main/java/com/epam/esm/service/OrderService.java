package com.epam.esm.service;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Page<OrderRepresentationDto> findUserOrders(String userId, Pageable pageable);

    Optional<OrderRepresentationDto> findUserOrderById(String userId, String orderId);

    List<OrderRepresentationDto> findMostExpensiveUserOrder(String userId);

    Optional<OrderRepresentationDto> create(User user, CreateOrderDto newOrder);

    long calculateTotalPrice(List<Long> certificatesId);
}
