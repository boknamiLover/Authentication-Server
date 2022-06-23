package com.example.jiniCo.common.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	
    @Bean
    public Docket restAPI() {

        return new Docket(DocumentationType.SWAGGER_2)
        		  .consumes(getConsumeContentTypes())
        		  .produces(getProduceContentTypes())
        		  .apiInfo(apiInfo())
        	      .apiInfo(apiInfo())
        	      .securityContexts(Arrays.asList(securityContext()))
        	      .securitySchemes(Arrays.asList(apiKey()))
        	      .select()
        	      .apis(RequestHandlerSelectors.basePackage("com.example.jiniCo"))
        	      .paths(PathSelectors.any())
        	      .build();
    }
    
    private Set<String> getConsumeContentTypes() {
    	Set<String> consumes = new HashSet<>();
    	consumes.add("application/json;charset=UTF-8");
    	return consumes;
    }
    
    private Set<String> getProduceContentTypes() {
    	Set<String> produces = new HashSet<>();
    	produces.add("application/json;charset=UTF-8");
    	return produces;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JiniCo Spring Boot REST API")
                .version("1.0.0")
                .description("JiniCo's swagger api")
                .build();
    }
    
    // header 세팅 용 
    private ApiKey apiKey() { 
        return new ApiKey("JWT", "Authorization", "header"); 
    }
    
    private SecurityContext securityContext() { 
        return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
    } 

    private List<SecurityReference> defaultAuth() { 
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
        authorizationScopes[0] = authorizationScope; 
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
    }
}
