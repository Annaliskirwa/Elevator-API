package com.lendtech.elevator.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppProperties {

    @Value("${app-properties.api-username}")
    private String apiUsername;

    @Value("${app-properties.api-password}")
    private String apiPassword;

}
