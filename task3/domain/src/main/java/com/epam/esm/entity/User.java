package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private LocalDateTime birthDay;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private BankAcc moneyAccount;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<CertificateOrder> certificateOrders;

    public boolean add(CertificateOrder certificateOrder) {
        return certificateOrders.add(certificateOrder);
    }
}
