package com.epam.esm.repository;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CertificateOrder, Long>, JpaSpecificationExecutor<CertificateOrder> {
    @Query(value = "select certificate_order.* from certificate_order " +
            "where total_price = (select max(total_price) from certificate_order) and user_id = 1;",
            nativeQuery = true)
    List<CertificateOrder> findMostExpensiveUserOrder(@Param("userId") Long userId);

    List<CertificateOrder> findAllByOwner(User user);
}
