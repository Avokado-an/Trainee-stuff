package com.epam.esm.model.dto;

import com.epam.esm.repository.type.SearchTypeQuery;
import com.epam.esm.repository.type.SortTypeQuery;
import lombok.Data;

import java.util.Set;

@Data
public class CertificateFilterDto {
    String tagName;
    String certificateNameOrDescription;
    Set<SortTypeQuery> sortingAlgorithm;
    Set<SearchTypeQuery> searchTypeQueries;
}
