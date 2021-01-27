package com.epam.esm.service;

import com.epam.esm.entity.BankAcc;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.CreateOrderDto;
import com.epam.esm.entity.dto.CreateUserDto;
import com.epam.esm.repository.BankAccRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImplementation;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.UserValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public UserService UserService() {
            return new UserServiceImplementation();
        }

        @Bean
        public UserValidator userValidator() {
            return new UserValidator();
        }
    }

    @MockBean
    private OrderService orderService;

    @MockBean
    private BankAccRepository bankAccRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderValidator orderValidator;

    @Autowired
    private UserService userService;

    @Test
    public void findAllUsersValidTest() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Page<User> users = userService.findAllUsers(Pageable.unpaged());
        assertEquals(0, users.getTotalElements());
    }

    @Test
    public void findByIdValidTest() {
        String id = "1";
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));
        Optional<User> user = userService.findById(id);
        assertTrue(user.isPresent());
    }

    @Test
    public void findByIdInvalidTest() {
        String id = "1sfdrgrdrg";
        Optional<User> user = userService.findById(id);
        assertFalse(user.isPresent());
    }

    @Test
    public void createUserValidTest() {
        String name = "name";
        LocalDateTime birthday = LocalDateTime.parse("2000-01-14T17:03:22");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().birthDay(birthday).username(name).build());
        when(bankAccRepository.save(any(BankAcc.class))).thenReturn(new BankAcc());
        CreateUserDto createdUser = new CreateUserDto();
        createdUser.setBirthday(birthday);
        createdUser.setUsername(name);
        Optional<User> user = userService.createUser(createdUser);
        assertTrue(user.isPresent());
    }

    @Test
    public void createUserInvalidTest() {
        String name = "name";
        LocalDateTime birthday = LocalDateTime.parse("1000-01-14T17:03:22");
        CreateUserDto createdUser = new CreateUserDto();
        createdUser.setBirthday(birthday);
        createdUser.setUsername(name);
        Optional<User> user = userService.createUser(createdUser);
        assertFalse(user.isPresent());
    }
}
