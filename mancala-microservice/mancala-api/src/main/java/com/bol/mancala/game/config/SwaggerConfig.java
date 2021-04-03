package com.bol.mancala.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(Predicate.isEqual(RequestHandlerSelectors.basePackage("com.bol.mancala.game.controller")))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("Manacala Game Api service")
                .description("This application provides api for building Mancala game application.")
                .version("1.0.0")
                .contact(new Contact("Salman Azad", "https://www.linkedin.com/in/salman-azad-5a4076125/", "salmanazad077@gmail.com"))
                .build();
    }
}
