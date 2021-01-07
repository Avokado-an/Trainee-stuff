package com.epam.esm.model.dto;

import com.epam.esm.model.type.SortTypeQuery;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CertificateFilterDto {
    String tagName;
    String certificateNameOrDescription;
    Set<SortTypeQuery> sortingAlgorithm;
}
