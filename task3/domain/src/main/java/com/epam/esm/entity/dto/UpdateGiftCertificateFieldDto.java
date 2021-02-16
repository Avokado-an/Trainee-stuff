package com.epam.esm.entity.dto;

import com.epam.esm.repository.type.CertificateFieldsType;
import lombok.Data;

@Data
public class UpdateGiftCertificateFieldDto {
    private Long certificateId;
    private CertificateFieldsType field;
    private String editedValue;
}
