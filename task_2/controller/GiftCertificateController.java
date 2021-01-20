package com.epam.esm.controller;

import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.dto.CertificateFilterDto;
import com.epam.esm.entity.dto.CreateGiftCertificateDto;
import com.epam.esm.entity.dto.IdDto;
import com.epam.esm.entity.dto.UpdateGiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Optional<GiftCertificate> showCertificates(@PathVariable String id) {
        return giftCertificateService.findById(id);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Set<GiftCertificate> deleteCertificate(@RequestBody IdDto idDto) {
        return giftCertificateService.delete(idDto.getId());
    }

    @PutMapping
    public Optional<GiftCertificate> updateCertificate(@RequestBody UpdateGiftCertificateDto updatedMessage) {
        return giftCertificateService.update(updatedMessage);
    }

    @PostMapping
    public Optional<GiftCertificate> addCertificate(@RequestBody CreateGiftCertificateDto newCertificate) {
        return giftCertificateService.create(newCertificate);
    }

    @PostMapping("/filter")
    public List<GiftCertificate> filterCertificates(@RequestBody CertificateFilterDto filterDto) {
        return giftCertificateService.filter(filterDto);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorHandler handleIncorrectParameterValueException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }
}
