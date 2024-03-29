package tajiri.example.jwt.auth.config;

import com.auth0.jwt.*;
import org.springframework.core.env.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.filter.*;
import tajiri.example.jwt.auth.model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static tajiri.example.jwt.auth.model.Constants.HEADER_AUTHORIZATION;
import static tajiri.example.jwt.auth.model.Constants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private Environment env;

    JWTAuthorizationFilter(UserDetailsService userDetailsService, Environment env) {
        this.userDetailsService = userDetailsService;
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        var header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        var token = header.replace(TOKEN_PREFIX, "");
        var username = getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(username);

            if (validateToken(username, userDetails)) {
                var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

    //TODO extract token manipulation
    private String getUsernameFromToken(String token) {
        // @formatter:off
        return JWT.require(HMAC512(env.getProperty("TOKEN.SECRET_KEY", Constants.DEFAULT_TOKEN_KEY)))
                    .build()
                    .verify(token)
                    .getSubject();
        // @formatter:on
    }

    //TODO extract token manipulation
    //FIXME check token is not expired
    private boolean validateToken(String username, UserDetails userDetails) {
        return userDetails.getUsername().equals(username);
    }

}
