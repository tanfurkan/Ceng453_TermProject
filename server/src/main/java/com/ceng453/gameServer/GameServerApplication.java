package com.ceng453.gameServer;

import com.ceng453.gameServer.controller.MultiPlayerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class GameServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GameServerApplication.class, args);
        Thread multiPlayerController = new Thread(new MultiPlayerController());
        multiPlayerController.start();
    }

    /**
     * This method configures Swagger by creating a Docket API instance
     *
     * @return New prepared instance of Docket for Swagger configuration
     */
    @Bean
    public Docket swaggerConfiguration() {
        // Returns prepared Docket instance
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/server_program7/api/*"))
                .apis(RequestHandlerSelectors.basePackage("com.ceng453"))
                .build()
                .apiInfo(apiDetails());
    }

    /**
     * This method creates API details for APi info part of prepared Docket instance.
     *
     * @return New API information instance filled by ApiInfoBuilder
     */
    private ApiInfo apiDetails() {
        return new ApiInfoBuilder()
                .title("Game Server for CENG 453 Term Project")
                .description("Sample API using Spring Boot, MariaDB and Swagger")
                .license("Free To Use")
                .version("0.1")
                .build();
    }
}
