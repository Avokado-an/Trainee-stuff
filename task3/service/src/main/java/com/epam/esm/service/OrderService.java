package com.epam.esm.service;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.dto.CreateOrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<CertificateOrder> findUserOrders(String userId);

    List<CertificateOrder> findAllOrders();

    Optional<CertificateOrder> findUserOrderById(String userId, String orderId);

    List<CertificateOrder> findMostExpensiveUserOrder(String userId);

    Optional<CertificateOrder> create(CreateOrderDto newOrder);

    long calculateTotalPrice(List<Long> certificatesId);
}
