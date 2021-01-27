package com.epam.esm.service;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImplementation;
import com.epam.esm.service.impl.OrderServiceImplementation;
import com.epam.esm.validator.OrderValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderServiceTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public OrderService orderService() {
            return new OrderServiceImplementation();
        }

        @Bean
        public OrderValidator orderValidator() {
            return new OrderValidator();
        }
    }

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private GiftCertificateServiceImplementation certificateService;

//List<GiftCertificate> certificates = Arrays.asList(GiftCertificate.builder().price(100L).build(),
    //        GiftCertificate.builder().price(100L).build());

    @Test
    public void findMostExpensiveUserOrderValidTest() {
        String userId = "1";
        when(orderRepository.findMostExpensiveUserOrder(any(Long.class))).thenReturn(Arrays
                .asList(new CertificateOrder(), new CertificateOrder()));
        List<CertificateOrder> orders = orderService.findMostExpensiveUserOrder(userId);
        assertEquals(2, orders.size());
    }

    @Test
    public void findMostExpensiveUserOrderInvalidTest() {
        String userId = "1QWERWEFSFL";
        List<CertificateOrder> orders = orderService.findMostExpensiveUserOrder(userId);
        assertEquals(0, orders.size());
    }

    @Test
    public void findUserOrderByIdValidTest() {
        String userId = "1";
        String orderId = "1";
        when(orderRepository.findByIdAndOwner(any(Long.class), any(User.class)))
                .thenReturn(Optional.of(new CertificateOrder()));
        Optional<CertificateOrder> order = orderService.findUserOrderById(userId, orderId);
        assertTrue(order.isPresent());
    }

    @Test
    public void findUserOrderByIdOrderInvalidTest() {
        String userId = "1QWERWEFSFL";
        String orderId = "werwfief";
        Optional<CertificateOrder> order = orderService.findUserOrderById(userId, orderId);
        assertFalse(order.isPresent());
    }

    @Test
    public void findUserOrdersValidTest() {
        String userId = "1";
        when(orderRepository.findAllByOwner(any(User.class), any(Pageable.class))).thenReturn(Page.empty());
        Page<CertificateOrder> orders = orderService.findUserOrders(userId, Pageable.unpaged());
        assertEquals(0, orders.getTotalElements());
    }

    @Test
    public void findUserOrdersInvalidTest() {
        String userId = "1QWERWEFSFL";
        Page<CertificateOrder> orders = orderService.findUserOrders(userId, Pageable.unpaged());
        assertEquals(0, orders.getTotalElements());
    }

    @Test
    public void calculateTotalPriceValidTest() {
        when(certificateService.findIdsCertificates(any(List.class)))
                .thenReturn(Arrays.asList(GiftCertificate.builder().price(100L).build(),
                        GiftCertificate.builder().price(120L).build()));
        long actualPrice = orderService.calculateTotalPrice(new ArrayList<>());
        long expectedPrice = 220L;
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    public void calculateTotalPriceInvalidTest() {
        when(certificateService.findIdsCertificates(any(List.class)))
                .thenReturn(Arrays.asList(GiftCertificate.builder().price(100L).build(),
                        GiftCertificate.builder().price(120L).build()));
        long actualPrice = orderService.calculateTotalPrice(new ArrayList<>());
        long expectedPrice = 230L;
        assertNotEquals(expectedPrice, actualPrice);
    }

    @Test
    public void createValidTest() {
        when(certificateService.findIdsCertificates(any(List.class)))
                .thenReturn(Arrays.asList(GiftCertificate.builder().price(100L).build(),
                        GiftCertificate.builder().price(120L).build()));
        when(orderRepository.save(any(CertificateOrder.class))).thenReturn(new CertificateOrder());
        CreateOrderDto orderDto = new CreateOrderDto();
        orderDto.setBuyerId(1L);
        orderDto.setOrderedCertificatesId(new ArrayList<>());
        Optional<CertificateOrder> actualOrder = orderService.create(orderDto);
        assertTrue(actualOrder.isPresent());
    }

    @Test
    public void createInvalidTest() {
        when(certificateService.findIdsCertificates(any(List.class)))
                .thenReturn(Arrays.asList(GiftCertificate.builder().price(10000000000L).build(),
                        GiftCertificate.builder().price(1200000000000000000L).build()));
        CreateOrderDto orderDto = new CreateOrderDto();
        orderDto.setBuyerId(1L);
        orderDto.setOrderedCertificatesId(new ArrayList<>());
        Optional<CertificateOrder> actualOrder = orderService.create(orderDto);
        assertFalse(actualOrder.isPresent());
    }
}
