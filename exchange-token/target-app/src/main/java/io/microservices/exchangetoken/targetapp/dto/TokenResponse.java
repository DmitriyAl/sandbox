// dto/TokenResponse.java
package io.microservices.exchangetoken.targetapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String refToken;
}