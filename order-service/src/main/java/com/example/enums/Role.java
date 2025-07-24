package com.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Role of the user : ADMIN or CUSTOMER")
public enum Role {
    @Schema(description = "ADMIN role with full access")
    ADMIN,
    @Schema(description = "CUSTOMER role with limited access")
    CUSTOMER
}
