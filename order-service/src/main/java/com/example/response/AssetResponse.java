package com.example.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetResponse {

    private Long id;

    private String assetName;

    private BigDecimal size;

    private BigDecimal usableSize;

    private Long customerId;

}
