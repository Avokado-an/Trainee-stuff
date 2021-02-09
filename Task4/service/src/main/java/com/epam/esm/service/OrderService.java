package com.epam.esm.service;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResultNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Page<OrderRepresentationDto> findUserOrders(String userId, Pageable pageable);

    OrderRepresentationDto findUserOrderById(String userId, String orderId) throws ResultNotFoundException;

    List<OrderRepresentationDto> findMostExpensiveUserOrder(String userId);

    OrderRepresentationDto create(User user, CreateOrderDto newOrder);

    OrderRepresentationDto createOrderInstance(CreateOrderDto newOrder);

    long calculateTotalPrice(List<Long> certificatesId);
}
