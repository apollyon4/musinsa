package com.musinsa.task.coordination.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Swagger API")
                .version("v1.0.0")
                .description("스웨거 API");

        return new OpenAPI()
                .info(info);
    }
}