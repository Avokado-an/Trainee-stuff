package com.epam.esm.repository;

import com.epam.esm.entity.BankAcc;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccRepository extends JpaRepository<BankAcc, Long> {
    Optional<BankAcc> findByUser(User user);
}
