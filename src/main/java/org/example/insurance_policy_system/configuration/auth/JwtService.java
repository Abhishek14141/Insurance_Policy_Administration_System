package org.example.insurance_policy_system.configuration.auth;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
@Service
public class JwtService {
    private final SecretKey key;
    private final long accessMs;
    public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.access-expiration-ms}") long accessMs){
        byte[] bytes;
        try{
            bytes=Base64.getDecoder().decode(secret);
        } catch(IllegalArgumentException e){
            bytes=secret.getBytes(StandardCharsets.UTF_8);
        }
        if(bytes.length<32) throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        this.key=Keys.hmacShaKeyFor(bytes);
        this.accessMs=accessMs;
    }
    public String generateAccessToken(String username,String role){
        Date now=new Date();
        return Jwts.builder().subject(username).claim("role",role).issuedAt(now).expiration(new Date(now.getTime()+accessMs)).signWith(key).compact();
    }
    public String username(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean valid(String token){
        try{
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().after(new Date());
        } catch(JwtException|IllegalArgumentException e){
            return false;
        }
    }
    public long accessSeconds(){
        return accessMs/1000;
    }
}
