package com.est.newstwin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger(OpenAPI 3) 설정
 * - JWT Authorization Header 인증 지원
 * - /swagger-ui/index.html 로 접근 가능
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String schemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(schemeName))

                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("JWT 토큰을 입력하세요. (예: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...)")
                        )
                )

                .info(new Info()
                        .title("NewsTwin API Docs")
                        .description("NewsTwin 프로젝트 REST API 명세서")
                        .version("1.0.0"));
    }
}
