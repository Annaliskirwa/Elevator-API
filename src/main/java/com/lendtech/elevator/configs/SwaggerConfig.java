package com.lendtech.elevator.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.stereotype.Component;

@Component
@OpenAPIDefinition(
        info = @Info(title = "lendtech-elevator-api",
                version = "1.0.0")
)
public class SwaggerConfig {
}
