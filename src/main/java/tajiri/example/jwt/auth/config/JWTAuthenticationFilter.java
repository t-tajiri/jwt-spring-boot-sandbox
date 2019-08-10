package tajiri.example.jwt.auth.config;

import com.auth0.jwt.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.core.env.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.*;
import tajiri.example.jwt.auth.model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.time.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static tajiri.example.jwt.auth.model.Constants.DEFAULT_TOKEN_EXPIRATION_DATE;
import static tajiri.example.jwt.auth.model.Constants.DEFAULT_TOKEN_KEY;
import static tajiri.example.jwt.auth.model.Constants.HEADER_AUTHORIZATION;
import static tajiri.example.jwt.auth.model.Constants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private Environment env;

    JWTAuthenticationFilter(AuthenticationManager authenticationManager, Environment env) {
        this.authenticationManager = authenticationManager;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            var loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);

            // @formatter:off
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                      loginUser.getUsername()
                    , loginUser.getPassword()
                )
            );
            // @formatter:on
        } catch (IOException e) {
            throw new RuntimeException("parsing request to object when attempt login", e);
        }
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication auth) {

        var secretKey = env.getProperty("TOKEN.SECRET_KEY", DEFAULT_TOKEN_KEY);

        var expirationTime = Long.parseLong(env.getProperty("TOKEN.EXPIRATION_TIME", DEFAULT_TOKEN_EXPIRATION_DATE));

        //TODO extract token manipulation
        // @formatter:off
        var token = JWT.create()
                        .withSubject(((User) auth.getPrincipal()).getUsername())
                        .withExpiresAt(Date.from(Instant.now().plusMillis(expirationTime)))
                        .sign(HMAC512(secretKey.getBytes()));
        // @formatter:on

        response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + token);
    }

}
