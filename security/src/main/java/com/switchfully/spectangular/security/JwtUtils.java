package com.switchfully.spectangular.security;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtUtils {
    static JwtBuilder buildFromUser(User user, Date expiration) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getId().toString())
                .setExpiration(expiration) // 1 hour
                .claim("role", user.getRole().toString());
    }

    static void applyToResponse(JwtBuilder builder, HttpServletResponse response) {
        var token = builder.compact();
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }

    static void updateIfChanged(Jws<Claims> token, User user, HttpServletResponse response) {
        String roleInToken = token.getBody().get("role", String.class);
        if (roleInToken == null) {
            throw new MalformedJwtException("Token should contain a role claim");
        }

        Role parsedRole = Role.valueOf(roleInToken);
        if (parsedRole == null) {
            throw new MalformedJwtException("Role claim in token does not match a known role");
        }

        if (!parsedRole.equals(user.getRole())) {
            var tokenBuilder = buildFromUser(user, token.getBody().getExpiration());
            applyToResponse(tokenBuilder, response);
        }
    }
}
