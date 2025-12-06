package com.main.jobilitybackend.jwtSecurity;

import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtProvider {


    public static SecretKey key = Keys.hmacShaKeyFor(Jwtconstants.getApiKey().getBytes());
    
    @SuppressWarnings("deprecation")
    public static String generateJwtToken(Authentication auth,long expirationTime){
            String jwt = Jwts.builder()
                        .setIssuer("ok").setIssuedAt(new Date())
                        .setExpiration(new Date(new Date().getTime()+expirationTime))
                        .claim("email", auth.getName())
                        .claim("role", auth.getAuthorities().toArray()[0].toString())
                        .signWith(key)
                        .compact();
        return jwt;
    }

    @SuppressWarnings("deprecation")
    public static String generateJwtToken(Authentication auth){
            String jwt = Jwts.builder()
                        .setIssuer("ok").setIssuedAt(new Date())
                        .setExpiration(new Date(new Date().getTime()+Jwtconstants.WEBEXPIRATION))
                        .claim("email", auth.getName())
                        .claim("role", auth.getAuthorities().toArray()[0].toString())
                        .signWith(key)
                        .compact();
        return jwt;
    }

    @SuppressWarnings("deprecation")
    public static String getEmailFromJwt(String jwt){

        Claims claims = Jwts.parser()
                    .setSigningKey(key).build()
                    .parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }

    @SuppressWarnings("deprecation")
    public static String getRoleFromJwt(String jwt){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(key).build()
                    .parseClaimsJws(jwt).getBody();
                        
        String role = claims.get("role").toString();
        return role;
        }catch(Exception e){
            return "not ok";
        }
    }
}
