package HospitalCasoSemestral.Habitacion.confi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Bean
    public SecurityFilterChain filtro(HttpSecurity http) throws Exception {http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/doc/swagger-ui.html",
                            "/doc/swagger-ui/**"
                    ).permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                    .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(
            PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("Maty")
                .password(encoder.encode("DuocUC2026"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


