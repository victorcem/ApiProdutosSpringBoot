package br.com.victordev.springboot.security;

import br.com.victordev.springboot.models.user.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserModel userModel){
        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(userModel.getLogin())
                    .withExpiresAt(genExpirationData())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validationToken(String token){
        try{
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTCreationException exception){
            return "";
        }
    }

    private Instant genExpirationData(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3:00"));
    }
}
