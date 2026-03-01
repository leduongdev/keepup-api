package com.ra.base_spring_boot.config.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.config.security.principle.CustomAccountDetailsService;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.exception.TokenRevokedException;
import com.ra.base_spring_boot.repository.BlacklistTokenRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccountDetailsService userDetailsService;
    private final BlacklistTokenRepo blacklistTokenRepo;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                if (blacklistTokenRepo.existsByToken(token)) {
                    throw new TokenRevokedException("Token has been revoked");
                }
                String email = jwtTokenProvider.getEmailFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filterChain.doFilter(request, response);

        }
        catch (TokenRevokedException ex) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Object> body = new ApiResponse<>();
            body.setSuccess(false);
            body.setMessage("Token is invalid or has been revoked");
            body.setErrors(List.of(Map.of(
                    "path", request.getRequestURI(),
                    "message", ex.getMessage()
            )));
            body.setTimestamp(LocalDateTime.now());

            mapper.writeValue(response.getWriter(), body);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
