package com.example.ventas_bodega.config;

import com.example.ventas_bodega.security.custom.CustomAccessDeniedHandler;
import com.example.ventas_bodega.security.custom.CustomAuthenticationEntryPoint;
import com.example.ventas_bodega.security.filter.JwtCookieTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtCookieTokenFilter jwtCookieTokenFilter;

    @Autowired
    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CustomAccessDeniedHandler customAccessDeniedHandler,
                          JwtCookieTokenFilter jwtCookieTokenFilter) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtCookieTokenFilter = jwtCookieTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(
                            "http://localhost:5173/",
                            "https://zavefy.com",
                            "https://www.zavefy.com",
                            "http://192.168.101.4:5173/",
                            "http://10.74.51.227:5173/",
                            "http://192.168.18.21:5173/",
                            "http://172.16.202.25:5173/"
                    ));
                    config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS", "PATCH"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        //.requestMatchers("/api/loan/**").hasAnyRole("ASESOR", "ADMIN")
                        //.requestMatchers("/api/maintenance/**").hasAuthority("PERMISSION_MAINTENANCE")
                        .requestMatchers("/api/print-jobs/**").permitAll()
                        .requestMatchers("/api/catalog/**").permitAll()
                        .requestMatchers("/api/option/**").permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtCookieTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
