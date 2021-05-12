package com.switchfully.spectangular.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchfully.spectangular.exceptions.User;
import com.switchfully.spectangular.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;

        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var login = getLogin(request);
        var email = login.getUsername();
        var password = login.getPassword();
        System.out.println("email: " + email);
        System.out.println("wachtwoord: " + password);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getPrincipal().toString());
        var token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getId().toString())
                .setExpiration(new Date(new Date().getTime() + 3600000)) // 1 hour
                .claim("features", List.of(authentication.getAuthorities()))
                .claim("role", user.getRole().toString())
                .claim("profileName", user.getProfileName())
                .claim("email", user.getEmail())
                .compact();

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

    }

    private LoginDto getLogin(HttpServletRequest request) {
        try {
            return new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not read body from request", e);
        }
    }




}
