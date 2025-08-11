package com.javathinked.example.demo_spring.security;

import com.javathinked.example.demo_spring.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /** On ne filtre pas ces chemins (public) */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
    String uri = request.getRequestURI();         // ex: /produits/auth/login (avec context-path)
    String sp  = request.getServletPath();        // ex: /auth/login (sans context-path)

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

    return uri.startsWith(request.getContextPath() + "/auth/")
        || uri.startsWith(request.getContextPath() + "/actuator/")
        || "/error".equals(sp)
        || sp.startsWith("/auth/")
        || sp.startsWith("/actuator/");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = authHeader.substring(7);

            if (jwtUtil.validate(token)) {
                String username = jwtUtil.extractUsername(token);
                List<String> roles = jwtUtil.extractRoles(token); // ex: ["ROLE_USER"]

                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
