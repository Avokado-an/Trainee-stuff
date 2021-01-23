package com.epam.esm.hateoas;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasCertificateManager {
    private static final String CERTIFICATES = "certificates";

    private HateoasCertificateManager() {
    }

    public static void manageSpecificCertificateLinks(Optional<GiftCertificate> certificate) {
        if (certificate.isPresent()) {
            Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                    .showCertificates(certificate.get().getId().toString())).withSelfRel();
            Link certificatesLink = linkTo(methodOn(GiftCertificateController.class)
                    .showCertificates(Pageable.unpaged())).withRel(CERTIFICATES);
            certificate.get().add(selfLink, certificatesLink);
        }
    }

    public static void manageCertificatesLinks(Page<GiftCertificate> pageCertificates) {
        for (GiftCertificate certificate : pageCertificates) {
            Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                    .showCertificates(certificate.getId().toString())).withSelfRel();
            certificate.add(selfLink);
        }
    }
}
