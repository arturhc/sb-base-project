package com.company.appname.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;

import lombok.Data;

@Data
@EnableSwagger2
@Configuration
@ConfigurationProperties(prefix = "app.swagger")
public class SwaggerConfig {

    private String name;
    private String version;
    private String description;

    private String maintainerName;
    private String maintainerEmail;
    private String maintainerUrl;

    @Bean
    public Docket placesDocket() {
        return docket("AppName", "/api-app-name/.*");
    }

    private Docket docket(String title, String path) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(title)

                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex(path))
                .build()

                .pathMapping("/")
                .apiInfo(info());
    }

    private ApiInfo info() {

        return new ApiInfoBuilder()
                .title(name)
                .description(description)
                .version(version)
                .contact(new Contact(
                        maintainerName,
                        maintainerUrl,
                        maintainerUrl
                )).build();
    }

}
