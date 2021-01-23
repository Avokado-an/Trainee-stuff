package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class GiftCertificate extends RepresentationModel<GiftCertificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "GiftCertificates_tags",
            joinColumns = {@JoinColumn(name = "gift_certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "Utag_id")}
    )
    @ToString.Exclude private Set<Tag> tags;

    @ManyToMany(mappedBy = "certificates")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<CertificateOrder> certificateOrders;

    private String name;
    private String description;
    private Long price;
    private Integer duration;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
