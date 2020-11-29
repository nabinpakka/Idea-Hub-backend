package com.example.IdeaHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:8080")
//                .tags(
//                        new Tag("Auth","Authentication process and all related to users"),
//                        new Tag("Publication","All about publications that users publish."),
//                        new Tag("Files","Getting files related to respective publications.")
//
//                )
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.IdeaHub"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Idea-Hub")
                .description("This is an api documentation of the intern-project \"Idea-Hub\". " +
                        "This documentation should help to navigate through the server side of Idea-Hub.")
                .version("V1.0")
                .build();
    }
}
