package HospitalCasoSemestral.Habitacion.confi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Enrutamiento a través del API Gateway común
                .addServersItem(new Server().url("http://localhost:8500"))
                .info(new Info()
                        .title("API de Habitaciones")
                        .version("1.0.0")
                        .description("Documentación de la API de las habitaciones para el sistema de hospital")
                        .contact(new Contact()
                                .name("Equipo técnico")
                                .email("comunicaciones.hgf@redsalud.gob.cl")
                                .url("https://www.hospitalfricke.cl/"))
                        .license(new License()
                                .name("Hospital Viña del Mar")
                                .url("https://www.hospitalfricke.cl/pacientes")))
                // Habilita el esquema de seguridad global para JWT
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}