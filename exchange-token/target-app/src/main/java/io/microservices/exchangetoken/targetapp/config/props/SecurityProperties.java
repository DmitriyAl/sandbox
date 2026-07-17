package io.microservices.exchangetoken.targetapp.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
    private String internalApiKey;
    private int tokenTtlSeconds;
    private List<String> allowedRedirectPrefixes;
}
