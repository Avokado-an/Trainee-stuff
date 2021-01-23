package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.dto.*;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.hateoas.HateoasCertificateManager.manageCertificatesLinks;
import static com.epam.esm.hateoas.HateoasCertificateManager.manageSpecificCertificateLinks;

@RestController
@RequestMapping("certificates")
public class GiftCertificateController {
    private GiftCertificateService giftCertificateService;
    private UserService userService;

    @Autowired
    public void setGiftCertificateService(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public Page<GiftCertificate> showCertificates(@PageableDefault(
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GiftCertificate> pageCertificates = giftCertificateService.findAll(pageable);
        manageCertificatesLinks(pageCertificates);
        return pageCertificates;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<GiftCertificate>> showCertificates(@PathVariable String id) {
        Optional<GiftCertificate> certificate = giftCertificateService.findById(id);
        manageSpecificCertificateLinks(certificate);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @DeleteMapping
    public Set<GiftCertificate> deleteCertificate(@RequestBody IdDto idDto) {
        return giftCertificateService.delete(idDto.getId());
    }

    @PutMapping
    public Optional<GiftCertificate> updateCertificate(@RequestBody UpdateGiftCertificateDto updatedCertificate) {
        return giftCertificateService.update(updatedCertificate);
    }

    @PostMapping
    public Optional<GiftCertificate> addCertificate(@RequestBody CreateGiftCertificateDto newCertificate) {
        return giftCertificateService.create(newCertificate);
    }

    @PostMapping("/filter")
    public List<GiftCertificate> filterCertificates(@RequestBody CertificateFilterDto filterDto) {
        return giftCertificateService.filter(filterDto);
    }

    @PutMapping("/edit")
    public Optional<GiftCertificate> updateCertificateField(@RequestBody UpdateGiftCertificateFieldDto updatedField) {
        return giftCertificateService.updateField(updatedField);
    }

    @PostMapping("/order")
    public Optional<User> orderCertificate(@RequestBody CreateOrderDto newOrder) {
        return userService.makeOrder(newOrder);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorHandler handleIncorrectParameterValueException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND);
    }
}
