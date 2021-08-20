package ru.uzaretskaya.todo.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.uzaretskaya.todo.auth.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static io.jsonwebtoken.Claims.SUBJECT;

@Component
@Log
public class JwtUtils {
    public static final String USER_KEY = "user";
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access_token-expiration}")
    private int accessTokenExpiration;

    public String createAccessToken(User user) {
        Date currentDate = new Date();

        Map claims = new HashMap<String, Object>();
        claims.put(USER_KEY, user);
        claims.put(SUBJECT, user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validate(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            log.log(Level.SEVERE, "Invalid jwt token: ", jwt);
        } catch (ExpiredJwtException e) {
            log.log(Level.SEVERE, "JWT token is expired: ", jwt);
        } catch (UnsupportedJwtException e) {
            log.log(Level.SEVERE, "JWT token is unsupported: ", jwt);
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "JWT claims string is empty: ", jwt);
        }

        return false;
    }

    public User getUser(String jwt) {
        Map map = (Map)Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().get(USER_KEY);
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(map, User.class);

        return user;
    }
}
