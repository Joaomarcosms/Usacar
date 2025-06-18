package br.com.usacar.vendas.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:8080/api/swagger-ui/index.html

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        //SEM adição de Segurança - JWT
        return new OpenAPI()
                .info(new Info()
                        .title("API - Gestão de Vendas de Carros Usados")
                        .contact(new Contact()
                                .name("Equipe UsaCar")
                                .email("faleconosco@usacar.com.br")
                                .url("usacar.com.br/vendas"))
                        .description("Sistema de Gestão de Vendas de Carros Usados")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação")
                        .url("https://www.usacar.com.br/vendas/docs/open-api"));
    }

/*
        //COM adição de Segurança - JWT
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT")))
               .info(new Info()
                        .title("API - Gestão de Vendas de Carros Usados")
                        .contact(new Contact()
                                .name("Equipe UsaCar")
                                .email("faleconosco@usacar.com.br")
                                .url("usacar.com.br/vendas"))
                        .description("Sistema de Gestão de Vendas de Carros Usados")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação")
                        .url("https://www.usacar.com.br/vendas/docs/open-api"));
    }
*/

}