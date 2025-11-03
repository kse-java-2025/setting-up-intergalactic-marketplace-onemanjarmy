package com.odanylchuk.cosmocatsmarketplace.exception;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamsViolationDetails {

    private String fieldName;

    private String reason;
}
