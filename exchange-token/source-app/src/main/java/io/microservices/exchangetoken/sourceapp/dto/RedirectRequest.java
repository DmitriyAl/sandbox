package io.microservices.exchangetoken.sourceapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RedirectRequest {
    @NotBlank(message = "Target path is required")
    private String target; // например, "/old/orders"
}