package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.dto.CertificateFilterDto;
import com.epam.esm.model.dto.CreateGiftCertificateDto;
import com.epam.esm.model.dto.IdDto;
import com.epam.esm.model.dto.UpdateGiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private GiftCertificateService giftCertificateService;

    @Autowired
    public void setGiftCertificateService(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public Set<GiftCertificate> showCertificates() {
        return giftCertificateService.findAll();
    }

    @GetMapping("/{id}")
    public GiftCertificate showCertificates(@PathVariable String id) {
        return giftCertificateService.findById(id).get();
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Set<GiftCertificate> deleteCertificate(@RequestBody IdDto idDto) {
        return giftCertificateService.delete(idDto.getId());
    }

    @PutMapping
    public GiftCertificate updateCertificate(@RequestBody UpdateGiftCertificateDto updatedMessage) {
        return giftCertificateService.update(updatedMessage).get();
    }

    @PostMapping
    public Set<GiftCertificate> addCertificate(@RequestBody CreateGiftCertificateDto newCertificate) {
        return giftCertificateService.create(newCertificate);
    }

    @PostMapping("/filter")
    public Set<GiftCertificate> filterCertificates(@RequestBody CertificateFilterDto filterDto) {
        return giftCertificateService.filter(filterDto);
    }
}
