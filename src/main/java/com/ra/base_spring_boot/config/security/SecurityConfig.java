package com.ra.base_spring_boot.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.config.security.jwt.JwtAuthFilter;
import com.ra.base_spring_boot.config.security.jwt.JwtTokenProvider;
import com.ra.base_spring_boot.config.security.mes.CustomAccessDeniedHandler;
import com.ra.base_spring_boot.config.security.mes.CustomAuthenticationEntryPoint;
import com.ra.base_spring_boot.config.security.principle.CustomAccountDetailsService;
import com.ra.base_spring_boot.repository.BlacklistTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccountDetailsService userDetailsService;
    private final BlacklistTokenRepo blacklistTokenRepo;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper mapper) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtTokenProvider, userDetailsService, blacklistTokenRepo, mapper);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // auth
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                // swagger
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(mapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(mapper))
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
