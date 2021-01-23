package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "owner")
@Builder
public class CertificateOrder extends RepresentationModel<CertificateOrder> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "Order_certificates",
            joinColumns = {@JoinColumn(name = "gift_certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "order_id")}
    )
    private List<GiftCertificate> certificates;

    private LocalDateTime creationTime;
    private Long totalPrice;
}