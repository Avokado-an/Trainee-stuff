package com.epam.esm.entity.dto.representation;

import com.epam.esm.entity.BankAcc;
import com.epam.esm.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class UserRepresentationDto extends RepresentationModel<OrderRepresentationDto> {
    private Long id;
    private String username;
    private LocalDateTime birthDay;
    private Set<UserRole> roles;
    private BankAcc moneyAccount;
}
