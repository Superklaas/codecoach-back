package com.switchfully.spectangular.security;

public class SecurityConstants {

    public static final String AUTH_LOGIN_URL ="/authenticate";

    public static final String JWT_SECRET = "p2s5v8y/B?E(H+MbQeShVmYq3t6w9z$C&F)J@NcRfUjWnZr4u7x!A%D*G-KaPdSg";

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
        throw new IllegalArgumentException("Cannot create instance of SecurityConstants (it's a 'static utility' class");
    }
}
