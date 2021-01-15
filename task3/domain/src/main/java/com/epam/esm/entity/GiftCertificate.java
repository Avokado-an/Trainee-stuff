package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "GiftCertificates_tags",
            joinColumns = {@JoinColumn(name = "gift_certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "Utag_id")}
    )
    private Set<Tag> tags;

    @ManyToMany(mappedBy = "certificates")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<CertificateOrder> certificateOrders;

    private String name;
    private String description;
    private long price;
    private int duration;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
