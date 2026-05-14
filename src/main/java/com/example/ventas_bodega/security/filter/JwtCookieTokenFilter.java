package com.example.ventas_bodega.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ventas_bodega.security.custom.CustomUserDetailService;
import com.example.ventas_bodega.security.custom.CustomUserDetails;
import com.example.ventas_bodega.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtCookieTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;

    public JwtCookieTokenFilter(JwtUtil jwtUtil, CustomUserDetailService customUserDetailService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if ("auth_token".equals(cookie.getName())) {
                    try {
                        String jwtToken = cookie.getValue();

                        DecodedJWT decodedJWT = jwtUtil.verifyToken(jwtToken);

                        String username = decodedJWT.getSubject();

                        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

                        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                        List<String> permisos = decodedJWT.getClaim("permissions").asList(String.class);

                        List<GrantedAuthority> authorities = new ArrayList<>();

                        if (roles != null) {
                            authorities.addAll(
                                    roles.stream()
                                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                            .toList()
                            );
                        }

                        if (permisos != null) {
                            authorities.addAll(
                                    permisos.stream()
                                            .map(perm -> new SimpleGrantedAuthority("PERMISSION_" + perm))
                                            .toList()
                            );
                        }

                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext().setAuthentication(auth);

                        // Opcional: agregar header
                        response.setHeader("X-User-Id", username);
                        response.setHeader("Authorization", "Bearer " + jwtToken);

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("text/plain;charset=UTF-8");
                        response.getWriter().write("Token inválido");
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
