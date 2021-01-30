package com.epam.esm.service.impl;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.entity.dto.representation.OrderRepresentationDto;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.OrderValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ModelMapper modelMapper;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
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
    @Transactional
    public Optional<OrderRepresentationDto> create(CreateOrderDto newOrder) {
        Optional<OrderRepresentationDto> createdOrder = Optional.empty();
        long totalPrice = calculateTotalPrice(newOrder.getOrderedCertificatesId());
        if (orderValidator.validateOrder(totalPrice)) {
            CertificateOrder certificateOrder = modelMapper.map(newOrder, CertificateOrder.class);
            User user = User.builder().id(newOrder.getBuyerId()).build();
            certificateOrder.setCertificates(createCertificatesFromId(newOrder.getOrderedCertificatesId()));
            certificateOrder.setOwner(user);
            certificateOrder.setCreationTime(LocalDateTime.now());
            certificateOrder.setTotalPrice(totalPrice);
            createdOrder = Optional.of(modelMapper
                    .map(orderRepository.save(certificateOrder), OrderRepresentationDto.class));
        }
        return createdOrder;
    }

    @Override
    public long calculateTotalPrice(List<Long> certificatesId) {
        List<GiftCertificate> certificates = certificateService.findIdsCertificates(certificatesId);
        return certificates.stream().map(GiftCertificate::getPrice).reduce(0L, Long::sum);
    }

    @Override
    @Transactional
    public Page<OrderRepresentationDto> findUserOrders(String userId, Pageable pageable) {
        Page<OrderRepresentationDto> orders;
        try {
            long userIdValue = Long.parseLong(userId);
            User user = User.builder().id(userIdValue).build();
            orders = orderRepository.findAllByOwner(user, pageable)
                    .map(o -> modelMapper.map(o, OrderRepresentationDto.class));
        } catch (NumberFormatException e) {
            orders = Page.empty();
        }
        return orders;
    }

    @Override
    @Transactional
    public Optional<OrderRepresentationDto> findUserOrderById(String userId, String orderId) {
        Optional<OrderRepresentationDto> orderRepresentation = Optional.empty();
        try {
            Long userIdValue = Long.parseLong(userId);
            long orderIdValue = Long.parseLong(orderId);
            User user = User.builder().id(userIdValue).build();
            Optional<CertificateOrder> order = orderRepository.findByIdAndOwner(orderIdValue, user);
            if (order.isPresent()) {
                orderRepresentation = Optional.of(modelMapper.map(order.get(), OrderRepresentationDto.class));
            }
        } catch (NumberFormatException e) {
            orderRepresentation = Optional.empty();
        }
        return orderRepresentation;
    }

    @Override
    @Transactional
    public List<OrderRepresentationDto> findMostExpensiveUserOrder(String userId) {
        List<OrderRepresentationDto> mostExpensiveOrder;
        try {
            long userIdValue = Long.parseLong(userId);
            mostExpensiveOrder = orderRepository.findMostExpensiveUserOrder(userIdValue).stream()
                    .map(o -> modelMapper.map(o, OrderRepresentationDto.class)).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            mostExpensiveOrder = new ArrayList<>();
        }
        return mostExpensiveOrder;
    }

    private List<GiftCertificate> createCertificatesFromId(List<Long> ids) {
        return ids.stream().map(i -> GiftCertificate.builder().id(i).build()).collect(Collectors.toList());
    }
}
