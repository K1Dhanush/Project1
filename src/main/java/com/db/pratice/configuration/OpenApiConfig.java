package com.db.pratice.configuration;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class OpenApiConfig
{
    @Bean
    public OpenAPI usersMicroserviceOpenAPI()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Credentails Management")
                        .description("Employee Credentails are secured properly using this Application")
                        .version("1.0"));
    }
}