package com.epam.esm.service;

import com.epam.esm.dto.LoginDto;
import com.epam.esm.entity.User;

public interface SecurityService {
    User loginUser(LoginDto loginDto);
}
