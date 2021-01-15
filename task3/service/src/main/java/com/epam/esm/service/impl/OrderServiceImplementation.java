package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.OrderValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService {
    private OrderRepository orderRepository;
    private OrderValidator orderValidator;
    private GiftCertificateServiceImplementation certificateService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderValidator(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    @Autowired
    public void setCertificateService(GiftCertificateServiceImplementation certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<CertificateOrder> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<CertificateOrder> create(CreateOrderDto newOrder) {
        Optional<CertificateOrder> createdOrder = Optional.empty();
        long totalPrice = calculateTotalPrice(newOrder.getOrderedCertificatesId());
        if (orderValidator.validateOrder(totalPrice)) {
            CertificateOrder certificateOrder = modelMapper.map(newOrder, CertificateOrder.class);
            User user = User.builder().id(newOrder.getBuyerId()).build();
            certificateOrder.setCertificates(createCertificatesFromId(newOrder.getOrderedCertificatesId()));
            certificateOrder.setOwner(user);
            certificateOrder.setCreationTime(LocalDateTime.now());
            certificateOrder.setTotalPrice(totalPrice);
            createdOrder = Optional.of(orderRepository.save(certificateOrder));
        }
        return createdOrder;
    }

    @Override
    public long calculateTotalPrice(List<Long> certificatesId) {
        List<GiftCertificate> certificates = certificateService.findIdsCertificates(certificatesId);
        return certificates.stream().map(GiftCertificate::getPrice).reduce(0L, Long::sum);
    }

    @Override
    public List<CertificateOrder> findUserOrders(String userId) {
        List<CertificateOrder> certificateOrders;
        try {
            long id = Long.parseLong(userId);
            User user = User.builder().id(id).build();
            certificateOrders = orderRepository.findAllByOwner(user);
        } catch (NumberFormatException e) {
            certificateOrders = new ArrayList<>();
        }
        return certificateOrders;
    }

    @Override
    public Optional<CertificateOrder> findUserOrderById(String userId, String orderId) {
        Optional<CertificateOrder> order;
        try {
            long userIdValue = Long.parseLong(userId);
            long orderIdValue = Long.parseLong(orderId);
            User user = User.builder().id(userIdValue).build();
            List<CertificateOrder> userCertificateOrders = orderRepository.findAllByOwner(user);
            order = userCertificateOrders.stream().filter(o -> Long.valueOf(o.getId()).equals(orderIdValue)).findAny();
        } catch (NumberFormatException e) {
            order = Optional.empty();
        }
        return order;
    }

    private List<GiftCertificate> createCertificatesFromId(List<Long> ids) {
        return ids.stream().map(i -> GiftCertificate.builder().id(i).build()).collect(Collectors.toList());
    }
}