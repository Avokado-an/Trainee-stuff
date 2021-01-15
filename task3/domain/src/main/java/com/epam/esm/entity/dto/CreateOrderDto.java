package com.epam.esm.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    private long buyerId;
    private List<Long> orderedCertificatesId;
}
