package com.example.IdeaHub.config.security;

import com.example.IdeaHub.auth.service.ApplicationUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;

@Service
public class JwtProvider {
    private Key key;

    //construct key only once for the session
    @PostConstruct
    public void init(){
        key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(Authentication authentication){
        ApplicationUserDetails userDetails = (ApplicationUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String jwt){
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);

            if(claimsJws.getSignature() ==null){
                return false;
            }
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUsernameFromJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }
}
