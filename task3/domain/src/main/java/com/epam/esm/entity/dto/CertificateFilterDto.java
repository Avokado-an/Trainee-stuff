package com.epam.esm.entity.dto;

import com.epam.esm.repository.type.SearchType;
import com.epam.esm.repository.type.SortType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CertificateFilterDto {
    List<String> tagNames;
    String certificateNameOrDescription;
    List<SortType> sortTypes;
    List<SearchType> searchTypes;
}
