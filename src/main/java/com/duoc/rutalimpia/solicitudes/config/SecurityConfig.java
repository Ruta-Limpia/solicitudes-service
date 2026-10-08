package com.duoc.rutalimpia.solicitudes.config;

import com.duoc.rutalimpia.solicitudes.security.InternalKeyFilter;
import com.duoc.rutalimpia.solicitudes.security.JwtAuthFilter;
import com.duoc.rutalimpia.solicitudes.security.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final HandlerExceptionResolver resolver;
    private final String internalKey;

    public SecurityConfig(JwtUtil jwtUtil,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
                          @Value("${app.internal.key}") String internalKey) {
        this.jwtUtil = jwtUtil;
        this.resolver = resolver;
        this.internalKey = internalKey;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/error").permitAll()
                        .requestMatchers("/api/v1/internal/**").hasRole("INTERNAL")
                        .anyRequest().authenticated())
                // Sin token -> 401 y rol incorrecto -> 403, ambos con ErrorResponse
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> resolver.resolveException(req, res, null, ex))
                        .accessDeniedHandler((req, res, ex) -> resolver.resolveException(req, res, null, ex)))
                .addFilterBefore(new InternalKeyFilter(internalKey, resolver), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
