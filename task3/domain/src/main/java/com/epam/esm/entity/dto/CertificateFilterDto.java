package com.epam.esm.entity.dto;

import com.epam.esm.repository.type.SearchType;
import com.epam.esm.repository.type.SortType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CertificateFilterDto {
    private List<String> tagNames;
    private String certificateNameOrDescription;
    private List<SortType> sortTypes;
    private List<SearchType> searchTypes;
}
