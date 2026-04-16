package com.ra.base_spring_boot.config.security;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.config.security.jwt.JwtAuthFilter;
import com.ra.base_spring_boot.config.security.jwt.JwtAuthenticationFilter;
import com.ra.base_spring_boot.config.security.jwt.JwtTokenProvider;
import com.ra.base_spring_boot.config.security.mes.CustomAccessDeniedHandler;
import com.ra.base_spring_boot.config.security.mes.CustomAuthenticationEntryPoint;
import com.ra.base_spring_boot.config.security.principle.CustomAccountDetailsService;
import com.ra.base_spring_boot.repository.BlacklistTokenRepo;
import com.ra.base_spring_boot.utils.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper mapper) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtTokenProvider, userDetailsService, blacklistTokenRepo, mapper);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(APIConstants.PUBLIC_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(mapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(mapper))
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
