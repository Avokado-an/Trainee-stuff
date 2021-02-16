package com.epam.esm.entity.dto;

import com.epam.esm.repository.type.SearchTypeQuery;
import com.epam.esm.repository.type.SortTypeQuery;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class CertificateFilterDto {
    private String tagName;
    private String certificateNameOrDescription;
    private Set<SortTypeQuery> sortingAlgorithm;
    private Set<SearchTypeQuery> searchTypeQueries;
}
