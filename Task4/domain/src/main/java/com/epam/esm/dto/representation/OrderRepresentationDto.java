package com.epam.esm.dto.representation;

import com.epam.esm.dto.status.OrderOperationStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class OrderRepresentationDto extends RepresentationModel<OrderRepresentationDto> {
    private Long id;
    private List<GiftCertificateRepresentationDto> certificates;
    private LocalDateTime creationTime;
    private Long totalPrice;
    private OrderOperationStatus status;
}
