package io.microservices.exchangetoken.sourceapp.component;

import io.microservices.exchangetoken.sourceapp.dto.LegacyTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class LegacySystemClient {

    private final RestTemplate restTemplate;

    @Value("${legacy.system-b.base-url}")
    private String baseUrl;

    @Value("${legacy.system-b.api-key}")
    private String apiKey;

    public LegacyTokenResponse requestOneTimeToken(String userId) {
        String url = baseUrl + "/internal/auth/impersonate?userId=" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Auth", apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<LegacyTokenResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                LegacyTokenResponse.class
        );
        return response.getBody();
    }
}