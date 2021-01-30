package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "GiftCertificates_tags",
            joinColumns = {@JoinColumn(name = "gift_certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "Utag_id")}
    )
    @ToString.Exclude private Set<Tag> tags;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CertificateOrder> certificateOrders;

    private String name;
    private String description;
    private Long price;
    private Integer duration;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
