package com.epam.esm.hateoas;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.representation.GiftCertificateRepresentationDto;
import com.epam.esm.exception.ResultNotFoundException;
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

    public static void manageSpecificCertificateLinks(GiftCertificateRepresentationDto certificate) throws ResultNotFoundException {
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .showCertificates(certificate.getId().toString())).withSelfRel();
        Link certificatesLink = linkTo(methodOn(GiftCertificateController.class)
                .showCertificates(Pageable.unpaged())).withRel(CERTIFICATES);
        certificate.add(selfLink, certificatesLink);

    }

    public static void manageCertificatesLinks(Page<GiftCertificateRepresentationDto> pageCertificates) throws ResultNotFoundException {
        for (GiftCertificateRepresentationDto certificate : pageCertificates) {
            Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                    .showCertificates(certificate.getId().toString())).withSelfRel();
            certificate.add(selfLink);
        }
    }
}
