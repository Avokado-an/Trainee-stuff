package com.epam.esm.repository;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CertificateOrder, Long>, JpaSpecificationExecutor<CertificateOrder> {
    List<CertificateOrder> findAllByOwner(User user);
}
