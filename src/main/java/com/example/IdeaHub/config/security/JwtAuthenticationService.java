package com.example.IdeaHub.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;

public class JwtAuthenticationService {
    public static Key key=Keys.secretKeyFor(SignatureAlgorithm.HS512);


    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username) {

        String jwt = createToken(username);

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
    }

    public static String createToken(String username) {
        String jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(key)
                .compact();

        return jwt;
    }

}
