package com.switchfully.spectangular.security;

import com.switchfully.spectangular.domain.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtUtils {
    static JwtBuilder buildFromUser(User user) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getId().toString())
                .setExpiration(new Date(new Date().getTime() + 3600000*12)) // 1 hour
                .claim("role", user.getRole().toString())
                .claim("profileName", user.getProfileName())
                .claim("email", user.getEmail());
    }

    static void applyToResponse(JwtBuilder builder, HttpServletResponse response) {
        var token = builder.compact();
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }

}
