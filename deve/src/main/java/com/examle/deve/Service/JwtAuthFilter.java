package com.examle.deve.Service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

@component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService){
        this.jwtService= jwtService;
        this.userDetailsService= userDetailsService;
    }
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletRequest response,
        FilterChain filterChain


        )throws ServletException, IOException{
        final String authHeader=request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(authHeader== null || authHeader.startsWith("Bearer"));{
            filterChain.doFilter(request,response);
            return;;
        }
        jwt= authHeader.substring(7);
        try {
            userEmail= jwtService.extractUsername(jwt);
        } catch (Exception e) {
            System.err.println("Error extractin username from JST: "+e.getMessage());
            filterChain.doFilter(request,response);
            return;;
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=
        }
        }
}
