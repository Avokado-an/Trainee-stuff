package com.epam.esm.service;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.dto.CreateOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Page<CertificateOrder> findUserOrders(String userId, Pageable pageable);

    Optional<CertificateOrder> findUserOrderById(String userId, String orderId);

    List<CertificateOrder> findMostExpensiveUserOrder(String userId);

    Optional<CertificateOrder> create(CreateOrderDto newOrder);

    long calculateTotalPrice(List<Long> certificatesId);
}
