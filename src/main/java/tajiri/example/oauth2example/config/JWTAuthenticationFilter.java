package tajiri.example.oauth2example.config;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tajiri.example.oauth2example.model.LoginUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private List<GrantedAuthority> roles;

    //TODO refactor extract constant
    private static final String SECRET_KEY = "t-tajiri";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);

            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                      loginUser.getUsername()
                    , loginUser.getPassword()
                    , roles
                )
            );
        } catch (IOException e) {
            throw new RuntimeException("parsing request to object when attempt login", e);
        }
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication auth) throws IOException, ServletException {
        // @formatter:off
        String token = JWT.create()
                          .withSubject(((LoginUser) auth.getPrincipal()).getUsername())
                          .withExpiresAt(Date.from(Instant.now()))
                          .sign(HMAC512(SECRET_KEY.getBytes()));
        // @formatter:on

        response.addHeader(HEADER_AUTHORIZATION, "Bearer " + token);
    }
}
