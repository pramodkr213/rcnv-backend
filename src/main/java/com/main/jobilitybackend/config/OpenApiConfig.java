package com.main.jobilitybackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Jobility Job Portal REST API",
        version = "1.0.0",
        description = """
                      Public API documentation for **Jobility**, an end-to-end job-matching platform.

                      **Quick-start:**
                      1. Authenticate via **`/api/auth/login`** to obtain your JWT access & refresh tokens.
                      2. Use `accessToken` cookie or `Authorization: Bearer <accessToken>` header in subsequent requests.
                      3. Call **`/api/auth/refresh`** when access token expires.
                      4. Refer to each endpoint’s schema for examples and required roles.
                      5. To view total number of APIs, call [**/api/public/api-count**](/api/public/api-count).
                      """,
        contact = @Contact(
            name = "Jobility API Support",
            email = "support@jobility.com",
            url = "https://jobility.com/support"
        )
    ),
    servers = {
        @Server(url = "https://api.jobility.com/", description = "Production"),
        @Server(url = "http://localhost:8080/", description = "Local development")
    }
)
public class OpenApiConfig {
}
