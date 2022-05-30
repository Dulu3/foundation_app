package com.piotrdulewski.foundationapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Component
public class JwtFilter extends BasicAuthenticationFilter {

    private static final String jwtSecret = "n4$lJ*jDAl<spjj";

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken authResult = getAuthorizationByToken(header);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthorizationByToken(String header) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;

        if (header != null) {
            try {
                Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecret.getBytes())
                        .parseClaimsJws(header.replace("Bearer ", ""));
                String username = claimsJws.getBody().getSubject();
                String role = claimsJws.getBody().get("role").toString();

                Set<SimpleGrantedAuthority> role1 = Collections.singleton(new SimpleGrantedAuthority(role));

                usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(username, null, role1);

            } catch (Exception exc) {
            }
        } else logger.error("Header was null");

        return usernamePasswordAuthenticationToken;
    }
}
