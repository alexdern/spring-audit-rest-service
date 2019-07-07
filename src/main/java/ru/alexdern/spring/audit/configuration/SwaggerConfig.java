package ru.alexdern.spring.audit.configuration;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";


    @Bean
    public Docket swaggerDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .forCodeGeneration(true)
                .produces(Collections.singleton("application/json"))
                .consumes(Collections.singleton("application/json"))
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                //.paths(PathSelectors.any())
                //.paths(PathSelectors.regex("/api"))
                .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Audit REST Service")
                .description("Liferay Audit")
                .version("1.0")
                //.termsOfServiceUrl("github")
                //.license("")
                //.licenseUrl("")
                //.contact(new Contact(""))
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("AuthToken", AuthTokenSecurityConfig.AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("global", "accessEverything"),
                /*
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations"),
                */
        };
        return Lists.newArrayList(new SecurityReference("AuthToken", scopes));
    }

}
