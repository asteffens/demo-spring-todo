package de.ast.demo.todo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration

public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.description}")
    public String appDesc;

    @Bean
    public OpenAPI openAPI(BuildProperties buildProperties) {
        SecurityRequirement s = new SecurityRequirement();
        s.addList("None");
        return new OpenAPI()
                .info(new Info().title(this.appName)
                        .description(this.appDesc)
                        .version(buildProperties.getVersion())
                        .contact(new Contact().name("Andreas Steffens").email("ast@interactive-pioneers.de"))
                        .license(new License().name("CC-BY 4.0").url("https://creativecommons.org/licenses/by/4.0/"))
                )
                .addSecurityItem(s);

    }
}