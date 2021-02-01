package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.dto.representation.GiftCertificateRepresentationDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.entity.User;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.CurrentPrincipalDefiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    private CurrentPrincipalDefiner principalDefiner;

    @Autowired
    public void setGiftCertificateService(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPrincipalDefiner(CurrentPrincipalDefiner principalDefiner) {
        this.principalDefiner = principalDefiner;
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
    @Secured("ROLE_ADMIN")
    public Set<GiftCertificateRepresentationDto> deleteCertificate(@RequestBody IdDto idDto) {
        return giftCertificateService.delete(idDto.getId());
    }

    @PutMapping
    @Secured("ROLE_ADMIN")
    public Optional<GiftCertificateRepresentationDto> updateCertificate(@RequestBody UpdateGiftCertificateDto updatedCertificate) {
        return giftCertificateService.update(updatedCertificate);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public Optional<GiftCertificateRepresentationDto> addCertificate(@RequestBody CreateGiftCertificateDto newCertificate) {
        return giftCertificateService.create(newCertificate);
    }

    @PostMapping("/filter")
    public List<GiftCertificateRepresentationDto> filterCertificates(@RequestBody CertificateFilterDto filterDto) {
        return giftCertificateService.filter(filterDto);
    }

    @PutMapping("/edit")
    @Secured("ROLE_ADMIN")
    public Optional<GiftCertificateRepresentationDto> updateField(@RequestBody UpdateGiftCertificateFieldDto updatedField) {
        return giftCertificateService.updateField(updatedField);
    }

    @PostMapping("/order")
    @Secured("ROLE_CLIENT")
    public Optional<OrderRepresentationDto> orderCertificate(@RequestBody CreateOrderDto newOrder) {
        String username = principalDefiner.currentUsername();
        return userService.makeOrder(username, newOrder);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleResourceNotFoundException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }
}
