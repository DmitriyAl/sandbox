package io.microservices.exchangetoken.sourceapp.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "legacy.system-b")
@Data
public class LegacyProperties {
    private String baseUrl;
    private String apiKey;
    private List<String> allowedRedirectPrefixes;
}