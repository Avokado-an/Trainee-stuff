package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.dto.representation.GiftCertificateRepresentationDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
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
    public Page<GiftCertificateRepresentationDto> showCertificates(@PageableDefault(
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GiftCertificateRepresentationDto> pageCertificates = giftCertificateService.findAll(pageable);
        manageCertificatesLinks(pageCertificates);
        return pageCertificates;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<GiftCertificateRepresentationDto>> showCertificates(@PathVariable String id) {
        Optional<GiftCertificateRepresentationDto> certificate = giftCertificateService.findById(id);
        manageSpecificCertificateLinks(certificate);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @DeleteMapping
    public Set<GiftCertificateRepresentationDto> deleteCertificate(@RequestBody IdDto idDto) {
        return giftCertificateService.delete(idDto.getId());
    }

    @PutMapping
    public Optional<GiftCertificateRepresentationDto> updateCertificate(@RequestBody UpdateGiftCertificateDto updatedCertificate) {
        return giftCertificateService.update(updatedCertificate);
    }

    @PostMapping
    public Optional<GiftCertificateRepresentationDto> addCertificate(@RequestBody CreateGiftCertificateDto newCertificate) {
        return giftCertificateService.create(newCertificate);
    }

    @PostMapping("/filter")
    public List<GiftCertificateRepresentationDto> filterCertificates(@RequestBody CertificateFilterDto filterDto) {
        return giftCertificateService.filter(filterDto);
    }

    @PutMapping("/edit")
    public Optional<GiftCertificateRepresentationDto> updateField(@RequestBody UpdateGiftCertificateFieldDto updatedField) {
        return giftCertificateService.updateField(updatedField);
    }

    @PostMapping("/order")
    public Optional<OrderRepresentationDto> orderCertificate(@RequestBody CreateOrderDto newOrder) {
        return userService.makeOrder(newOrder);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleResourceNotFoundException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }
}
