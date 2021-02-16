package com.epam.esm.service.impl;

import com.epam.esm.dto.representation.GiftCertificateRepresentationDto;
import com.epam.esm.dto.status.OrderOperationStatus;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.exception.ResultNotFoundException;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public OrderRepresentationDto create(User user, CreateOrderDto newOrder) {
        OrderRepresentationDto createdOrder;
        long totalPrice = calculateTotalPrice(newOrder.getOrderedCertificatesId());
        if (orderValidator.validateOrder(totalPrice) && doAllCertificatesExist(newOrder.getOrderedCertificatesId())) {
            createdOrder = createOrder(newOrder, totalPrice, user);
            createdOrder.setStatus(OrderOperationStatus.CREATED);
        } else {
            createdOrder = processInvalidOrder(newOrder, totalPrice);
        }
        return createdOrder;
    }

    @Override
    public long calculateTotalPrice(List<Long> certificatesId) {
        List<GiftCertificate> certificates = certificateService.findIdsCertificates(certificatesId);
        return certificates.stream().filter(o -> !Objects.isNull(o)).map(GiftCertificate::getPrice).reduce(0L, Long::sum);
    }

    @Override
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
    public OrderRepresentationDto findUserOrderById(String userId, String orderId) throws ResultNotFoundException {
        OrderRepresentationDto orderRepresentation;
        try {
            Long userIdValue = Long.parseLong(userId);
            long orderIdValue = Long.parseLong(orderId);
            User user = User.builder().id(userIdValue).build();
            Optional<CertificateOrder> order = orderRepository.findByIdAndOwner(orderIdValue, user);
            if(order.isPresent()) {
                orderRepresentation = order.map(certificateOrder ->
                        modelMapper.map(certificateOrder, OrderRepresentationDto.class)).get();
            } else {
                throw new ResultNotFoundException("Order was not found");
            }
        } catch (NumberFormatException e) {
            throw new ResultNotFoundException("Invalid id of user or certificate (should contain only numbers");
        }
        return orderRepresentation;
    }

    @Override
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

    @Override
    public OrderRepresentationDto createOrderInstance(CreateOrderDto newOrder) {
        OrderRepresentationDto order = new OrderRepresentationDto();
        order.setCertificates(createCertificatesFromId(newOrder.getOrderedCertificatesId()).stream().map(o ->
                modelMapper.map(o, GiftCertificateRepresentationDto.class)).collect(Collectors.toList()));
        order.setTotalPrice(calculateTotalPrice(newOrder.getOrderedCertificatesId()));
        return order;
    }

    private List<GiftCertificate> createCertificatesFromId(List<Long> ids) {
        return ids.stream().map(i -> GiftCertificate.builder().id(i).build()).collect(Collectors.toList());
    }

    private boolean doAllCertificatesExist(List<Long> ids) {
        for(Long id : ids) {
            try {
                certificateService.findById(id.toString());
            } catch (ResultNotFoundException e){
                return false;
            }
        }
        return true;
    }

    private OrderRepresentationDto createOrder(CreateOrderDto newOrder, long totalPrice, User user) {
        CertificateOrder certificateOrder = new CertificateOrder();
        certificateOrder.setCertificates(createCertificatesFromId(newOrder.getOrderedCertificatesId()));
        certificateOrder.setCreationTime(LocalDateTime.now());
        certificateOrder.setTotalPrice(totalPrice);
        user.addOrder(certificateOrder);
        certificateOrder.setOwner(user);
        return modelMapper.map(orderRepository.save(certificateOrder), OrderRepresentationDto.class);
    }

    private OrderRepresentationDto processInvalidOrder(CreateOrderDto newOrder, long totalPrice) {
        OrderRepresentationDto createdOrder = createOrderInstance(newOrder);
        if(!orderValidator.validateOrder(totalPrice)) {
            createdOrder.setStatus(OrderOperationStatus.TOTAL_PRICE_LARGER_THAN_10000);
        } else if(!doAllCertificatesExist(newOrder.getOrderedCertificatesId())) {
            createdOrder.setStatus(OrderOperationStatus.CERTIFICATES_ID_NOT_FOUND);
        } else {
            createdOrder.setStatus(OrderOperationStatus.CERTIFICATES_NOT_FOUND_AND_PRICE_LARGER_THAN_10000);
        }
        return createdOrder;
    }
}
