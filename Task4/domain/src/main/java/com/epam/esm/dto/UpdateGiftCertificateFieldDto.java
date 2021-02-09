package com.epam.esm.dto;

import com.epam.esm.repository.type.CertificateFieldsType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateGiftCertificateFieldDto {
    private Long certificateId;
    @NotNull
    private CertificateFieldsType field;
    @NotNull
    private String editedValue;
}
