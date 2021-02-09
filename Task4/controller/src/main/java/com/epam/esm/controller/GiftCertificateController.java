package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.dto.representation.GiftCertificateRepresentationDto;
import com.epam.esm.dto.representation.OrderRepresentationDto;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.exception.ResultNotFoundException;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws ResultNotFoundException {
        Page<GiftCertificateRepresentationDto> pageCertificates = giftCertificateService.findAll(pageable);
        manageCertificatesLinks(pageCertificates);
        return pageCertificates;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateRepresentationDto> showCertificates(@PathVariable String id)
            throws ResultNotFoundException {
        GiftCertificateRepresentationDto certificate = giftCertificateService.findById(id);
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
    public Optional<GiftCertificateRepresentationDto> updateCertificate(@Valid @RequestBody UpdateGiftCertificateDto updatedCertificate) {
        return giftCertificateService.update(updatedCertificate);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public GiftCertificateRepresentationDto addCertificate(@Valid @RequestBody CreateGiftCertificateDto newCertificate) {
        return giftCertificateService.create(newCertificate);
    }

    @PostMapping("/filter")
    public List<GiftCertificateRepresentationDto> filterCertificates(@RequestBody CertificateFilterDto filterDto) {
        return giftCertificateService.filter(filterDto);
    }

    @PutMapping("/edit")
    @Secured("ROLE_ADMIN")
    public Optional<GiftCertificateRepresentationDto> updateField(@Valid @RequestBody UpdateGiftCertificateFieldDto updatedField) {
        return giftCertificateService.updateField(updatedField);
    }

    @PostMapping("/order")
    @Secured("ROLE_CLIENT")
    public OrderRepresentationDto orderCertificate(@Valid @RequestBody CreateOrderDto newOrder) {
        String username = principalDefiner.currentUsername();
        return userService.makeOrder(username, newOrder);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResultNotFoundException.class)
    public ErrorHandler handleResultNotFoundExceptions(ResultNotFoundException ex) {
        return new ErrorHandler(ex.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }
}
