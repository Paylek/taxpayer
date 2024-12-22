package com.sviryd.taxpayer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "taxpayer")
public class TaxpayerConfig {
    private String addressAndDateURL;
    private String fullNameAndStatusURL;
}
