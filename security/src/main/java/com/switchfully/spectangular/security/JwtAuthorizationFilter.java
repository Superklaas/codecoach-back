package com.switchfully.spectangular.security;

import com.switchfully.spectangular.domain.Feature;
import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.exceptions.UnauthorizedException;
import com.switchfully.spectangular.services.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final UserService userService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication;

        try {
             authentication = getAuthentication(request, response);
        } catch (HttpClientErrorException e) {
            response.setStatus(401);
            response.setHeader("WWW-Authenticate", "JWT Invalid");
            return;
        }

        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var token = request.getHeader(SecurityConstants.TOKEN_HEADER);

        if (!isEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                var signingKey = SecurityConstants.JWT_SECRET.getBytes();

                var parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer ",""));

                var id = parsedToken
                        .getBody()
                        .getSubject();

                User user;
                try {
                    user = userService.findUserById(Integer.parseInt(id));
                } catch (Exception e) {
                    throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Subject does not exist");
                }

                JwtUtils.updateIfChanged(parsedToken, user, response);


                var authorities = Feature.getForRole(user.getRole())
                        .stream()
                        .map(Feature::toString)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                if (!isEmpty(id)) {
                    return new UsernamePasswordAuthenticationToken(id,null, authorities);
                }
            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "JWT Token faulty");
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "JWT Token faulty");
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "JWT Token faulty");
            } catch (SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "JWT Token faulty");
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "JWT Token faulty");
            }
        }

        return null;
    }
}
