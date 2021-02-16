package com.epam.esm.service.specification;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class GiftCertificateSpecification {
    public static Specification<GiftCertificate> filterCertificatesByTag(String tagName) {
        return (root, query, builder) -> {
            Join<GiftCertificate, Tag> certificateTagJoin = root.join("tags");
            return builder.isTrue(certificateTagJoin.get("name").in(tagName));
        };
    }

    public static Specification<GiftCertificate> filterCertificatesByNameDescriptionStart(String nameDescriptionBeginning) {
        return (root, query, builder) -> builder.or(builder.like(root.get("name"), nameDescriptionBeginning + "%"),
                builder.like(root.get("description"), nameDescriptionBeginning + "%"));
    }
}
